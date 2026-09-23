"use client";

import { useState } from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useStore } from "@/lib/store";
import { Heart, ArrowRight, AlertCircle, CheckCircle2 } from "lucide-react";

export default function LoginPage() {
  const [email, setEmail] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const { login } = useStore();
  const router = useRouter();

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!email) {
      setError("Please enter your registered email address.");
      return;
    }
    setError(null);
    setLoading(true);
    const res = await login(email);
    setLoading(false);
    if (res.success) {
      router.push("/dashboard");
    } else {
      setError(res.message);
    }
  };

  return (
    <div className="min-h-[80vh] flex items-center justify-center px-4 py-12">
      <div className="max-w-md w-full bg-white rounded-2xl border border-slate-200 shadow-xl p-8 space-y-6">
        <div className="text-center space-y-2">
          <div className="w-12 h-12 bg-green-100 text-green-600 rounded-xl flex items-center justify-center mx-auto">
            <Heart className="w-6 h-6 fill-green-600" />
          </div>
          <h1 className="text-2xl font-bold text-slate-900">Sign in to MealBridge</h1>
          <p className="text-xs text-slate-500">Access your food donations, requests, and community stats</p>
        </div>

        {error && (
          <div className="p-3 bg-red-50 border border-red-200 text-red-700 text-xs rounded-xl flex items-center gap-2">
            <AlertCircle className="w-4 h-4 shrink-0" />
            <span>{error}</span>
          </div>
        )}

        <form onSubmit={handleLogin} className="space-y-4">
          <div>
            <label className="block text-xs font-semibold text-slate-700 mb-1">
              Email Address
            </label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="e.g. donor@mealbridge.org or admin@mealbridge.org"
              className="w-full px-3.5 py-2.5 rounded-xl border border-slate-300 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
              required
            />
          </div>

          <div className="bg-slate-50 p-3 rounded-xl border border-slate-200 text-[11px] text-slate-600 space-y-1">
            <div className="font-semibold text-slate-700">Quick Demo Accounts:</div>
            <div>Admin: <span className="font-mono text-green-700 font-semibold cursor-pointer underline" onClick={() => setEmail("admin@mealbridge.org")}>admin@mealbridge.org</span></div>
            <div>Donor: <span className="font-mono text-green-700 font-semibold cursor-pointer underline" onClick={() => setEmail("donor@mealbridge.org")}>donor@mealbridge.org</span></div>
            <div>Seeker: <span className="font-mono text-green-700 font-semibold cursor-pointer underline" onClick={() => setEmail("seeker@mealbridge.org")}>seeker@mealbridge.org</span></div>
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full py-3 bg-green-600 hover:bg-green-700 text-white font-semibold text-sm rounded-xl shadow-md transition-colors flex items-center justify-center gap-2"
          >
            {loading ? "Signing in..." : "Continue to Dashboard"}
            <ArrowRight className="w-4 h-4" />
          </button>
        </form>

        <div className="text-center text-xs text-slate-500">
          Don&apos;t have an account?{" "}
          <Link href="/register" className="font-semibold text-green-600 hover:underline">
            Register here
          </Link>
        </div>
      </div>
    </div>
  );
}
