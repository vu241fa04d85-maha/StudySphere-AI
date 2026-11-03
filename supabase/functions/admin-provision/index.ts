// Supabase Edge Function: admin-provision
// Secure server-side administrator onboarding & role assignment (Super Admin Only)

import { serve } from "https://deno.land/std@0.168.0/http/server.ts";
import { createClient } from "https://esm.sh/@supabase/supabase-js@2";

const corsHeaders = {
  "Access-Control-Allow-Origin": "*",
  "Access-Control-Allow-Headers": "authorization, x-client-info, apikey, content-type",
};

serve(async (req) => {
  if (req.method === "OPTIONS") {
    return new Response("ok", { headers: corsHeaders });
  }

  try {
    const supabaseAdmin = createClient(
      Deno.env.get("SUPABASE_URL") ?? "",
      Deno.env.get("SUPABASE_SERVICE_ROLE_KEY") ?? "",
      { auth: { autoRefreshToken: false, persistSession: false } }
    );

    // Verify requesting caller is authenticated
    const authHeader = req.headers.get("Authorization");
    if (!authHeader) {
      return new Response(JSON.stringify({ error: "Missing authorization" }), { status: 401, headers: corsHeaders });
    }

    const { data: { user }, error: userError } = await supabaseAdmin.auth.getUser(authHeader.replace("Bearer ", ""));
    if (userError || !user) {
      return new Response(JSON.stringify({ error: "Unauthorized" }), { status: 401, headers: corsHeaders });
    }

    // Verify caller is super_admin in admin_profiles
    const { data: adminRecord, error: adminErr } = await supabaseAdmin
      .from("admin_profiles")
      .select("role, status")
      .eq("auth_user_id", user.id)
      .single();

    if (adminErr || !adminRecord || adminRecord.role !== "super_admin" || adminRecord.status !== "active") {
      return new Response(JSON.stringify({ error: "Forbidden: Super Admin access required" }), { status: 403, headers: corsHeaders });
    }

    const { adminCode, fullName, email, role } = await req.json();

    if (!adminCode || !email || !fullName) {
      return new Response(JSON.stringify({ error: "Missing required admin fields" }), { status: 400, headers: corsHeaders });
    }

    // Generate random secure temporary 16-character password
    const tempPassword = "Temp@" + crypto.randomUUID().slice(0, 10) + "!";

    // Create user via Supabase Auth Admin API
    const { data: newAuthUser, error: createError } = await supabaseAdmin.auth.admin.createUser({
      email,
      password: tempPassword,
      email_confirm: true,
      user_metadata: { role: "admin", admin_code: adminCode },
    });

    if (createError) {
      return new Response(JSON.stringify({ error: createError.message }), { status: 400, headers: corsHeaders });
    }

    // Insert or update in public.admin_profiles
    const { error: profileErr } = await supabaseAdmin
      .from("admin_profiles")
      .upsert({
        auth_user_id: newAuthUser.user.id,
        admin_code: adminCode,
        full_name: fullName,
        email: email,
        role: role || "academic_admin",
        status: "active",
        must_change_password: true,
      }, { onConflict: "admin_code" });

    if (profileErr) {
      return new Response(JSON.stringify({ error: profileErr.message }), { status: 500, headers: corsHeaders });
    }

    // Log to audit_logs
    await supabaseAdmin.from("audit_logs").insert({
      admin_code: "ADM001",
      action: "PROVISION_ADMIN",
      target_entity: "admin_profiles",
      target_id: adminCode,
      details: { email, role },
    });

    return new Response(
      JSON.stringify({
        success: true,
        message: `Admin ${adminCode} created. Deliver temporary credentials securely.`,
        adminCode,
        tempPassword, // Return temporary password to Super Admin ONLY on creation
      }),
      { status: 200, headers: { ...corsHeaders, "Content-Type": "application/json" } }
    );
  } catch (err: any) {
    return new Response(JSON.stringify({ error: err.message || "Failed to provision admin" }), { status: 500, headers: corsHeaders });
  }
});
