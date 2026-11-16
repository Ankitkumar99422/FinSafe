
import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "FinSafe | Plan Smart. Spend Safe.",
  description:
    "Make smarter financial decisions with FinSafe.",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body>{children}</body>
    </html>
  );
}
