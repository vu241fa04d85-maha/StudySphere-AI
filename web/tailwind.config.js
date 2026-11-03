/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        lavender: {
          light: '#F4EFFF',
          DEFAULT: '#B8A4F4',
          deep: '#6C42C4',
        },
        chartreuse: {
          light: '#F4FAD9',
          DEFAULT: '#D6F36A',
          deep: '#556B16',
        },
        tomato: {
          DEFAULT: '#FF6347',
          deep: '#C34630',
        },
        ivory: '#FFFEF8',
        surface: {
          glass: 'rgba(255, 255, 255, 0.65)',
          glassSolid: '#FFFFFF',
          glassHover: 'rgba(255, 255, 255, 0.85)',
          border: 'rgba(255, 255, 255, 0.85)',
          borderSubtle: 'rgba(184, 164, 244, 0.25)',
        },
        text: {
          primary: '#251C38',
          secondary: '#645B74',
          muted: '#746589',
        }
      },
      fontFamily: {
        sans: ['"DM Sans"', 'sans-serif'],
        heading: ['"Manrope"', 'sans-serif'],
        mono: ['"JetBrains Mono"', 'monospace'],
      },
      boxShadow: {
        glass: '0 8px 32px rgba(92, 68, 132, 0.08)',
        glassHover: '0 12px 40px rgba(92, 68, 132, 0.14)',
        glassFloating: '0 16px 48px rgba(92, 68, 132, 0.16)',
      },
      borderRadius: {
        'glass': '20px',
      }
    },
  },
  plugins: [],
}
