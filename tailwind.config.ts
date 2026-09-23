import type { Config } from "tailwindcss";

const config: Config = {
  content: [
    "./src/pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./src/components/**/*.{js,ts,jsx,tsx,mdx}",
    "./src/app/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  theme: {
    extend: {
      colors: {
        brand: {
          green: "#16A34A",
          greenDark: "#15803D",
          greenLight: "#DCFCE7",
          orange: "#EA580C",
          orangeHover: "#C2410C",
          orangeLight: "#FFEDD5",
          blue: "#2563EB",
          blueLight: "#DBEAFE",
        },
      },
    },
  },
  plugins: [],
};
export default config;
