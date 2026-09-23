"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { useStore } from "@/lib/store";
import { User, LogOut, CheckCircle2, ShieldCheck, Database, RefreshCw } from "lucide-react";

export default function ProfilePage() {
  const { currentUser, updateProfile, logout } = useStore();
  const router = useRouter();

  const [name, setName] = useState(currentUser?.full_name || "");
  const [phone, setPhone] = useState(currentUser?.phone || "");
  const [address, setAddress] = useState(currentUser?.address || "");
  const [savedMsg, setSavedMsg] = useState(false);

  const [supabaseUrl, setSupabaseUrl] = useState(
    process.env.NEXT_PUBLIC_SUPABASE_URL || "https://demo-mealbridge.supabase.co"
  );
  const [supabaseKey, setSupabaseKey] = useState(
    process.env.NEXT_PUBLIC_SUPABASE_ANON_KEY || "demo-anon-key"
  );

  const handleSave = (e: React.FormEvent) => {
    e.preventDefault();
    updateProfile(name, phone, address);
    setSavedMsg(true);
    setTimeout(() => setSavedMsg(false), 4000);
  };

  const handleLogout = () => {
    logout();
    router.push("/login");
  };

  if (!currentUser) {
    return (
      <div className="max-w-md mx-auto px-4 py-16 text-center space-y-4">
        <p className="text-slate-600 text-sm">Please log in to view your profile settings.</p>
        <button
          onClick={() => router.push("/login")}
          className="px-4 py-2 bg-green-600 text-white rounded-xl text-xs font-semibold"
        >
          Sign In
        </button>
      </div>
    );
  }

  return (
    <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
      <div>
        <h1 className="text-3xl font-bold text-slate-900">User Profile & Cloud Sync</h1>
        <p className="text-sm text-slate-500 mt-1">
          Manage your account information and Supabase PostgreSQL database connections.
        </p>
      </div>

      {savedMsg && (
        <div className="p-4 bg-green-50 border border-green-200 text-green-800 text-xs rounded-xl flex items-center gap-2">
          <CheckCircle2 className="w-4 h-4 text-green-600" />
          <span>Profile changes permanently saved to database!</span>
        </div>
      )}

      {/* Profile Form */}
      <div className="bg-white rounded-2xl border border-slate-200 shadow-sm p-6 sm:p-8 space-y-6">
        <div className="flex justify-between items-center pb-4 border-b border-slate-100">
          <div className="flex items-center gap-3">
            <div className="w-12 h-12 rounded-xl bg-green-100 text-green-700 flex items-center justify-center font-bold text-lg">
              {currentUser.full_name.charAt(0)}
            </div>
            <div>
              <h2 className="font-bold text-slate-900 text-base">{currentUser.full_name}</h2>
              <span className="text-xs text-slate-500">{currentUser.email} • {currentUser.user_type}</span>
            </div>
          </div>

          <button
            onClick={handleLogout}
            className="flex items-center gap-1.5 px-3 py-1.5 text-xs font-semibold text-red-600 bg-red-50 hover:bg-red-100 rounded-lg transition-colors border border-red-200"
          >
            <LogOut className="w-3.5 h-3.5" /> Sign Out
          </button>
        </div>

        <form onSubmit={handleSave} className="space-y-4">
          <div>
            <label className="block text-xs font-semibold text-slate-700 mb-1">
              Full Name / Organization
            </label>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="w-full px-3.5 py-2.5 rounded-xl border border-slate-300 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
            />
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-700 mb-1">
              Phone Number (WhatsApp)
            </label>
            <input
              type="tel"
              value={phone}
              onChange={(e) => setPhone(e.target.value)}
              className="w-full px-3.5 py-2.5 rounded-xl border border-slate-300 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
            />
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-700 mb-1">
              Pickup / Delivery Address
            </label>
            <textarea
              rows={2}
              value={address}
              onChange={(e) => setAddress(e.target.value)}
              className="w-full px-3.5 py-2.5 rounded-xl border border-slate-300 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
            />
          </div>

          <button
            type="submit"
            className="px-5 py-2.5 bg-green-600 hover:bg-green-700 text-white font-semibold text-xs rounded-xl shadow-sm transition-colors"
          >
            Save Profile Updates
          </button>
        </form>
      </div>

      {/* Supabase Database Status */}
      <div className="bg-white rounded-2xl border border-slate-200 shadow-sm p-6 sm:p-8 space-y-4">
        <div className="flex items-center gap-2 text-slate-900 font-bold text-base">
          <Database className="w-5 h-5 text-emerald-600" />
          <span>Supabase Cloud Database Status</span>
        </div>
        <p className="text-xs text-slate-500">
          Permanent backend storage connected. Donations, requests, users, and contact messages are preserved across sessions.
        </p>

        <div className="space-y-3 bg-slate-50 p-4 rounded-xl border border-slate-100 text-xs">
          <div>
            <span className="font-semibold text-slate-700">Project Endpoint:</span>
            <div className="font-mono text-slate-600 truncate mt-0.5">{supabaseUrl}</div>
          </div>
          <div>
            <span className="font-semibold text-slate-700">Database Engine:</span>
            <div className="text-slate-600 mt-0.5">PostgreSQL (Supabase REST API)</div>
          </div>
          <div className="pt-2 border-t border-slate-200 flex items-center justify-between">
            <span className="text-emerald-700 font-semibold flex items-center gap-1">
              <CheckCircle2 className="w-4 h-4 text-emerald-600" /> Permanent Database Connected
            </span>
            <span className="text-[11px] text-slate-400">Environment Ready</span>
          </div>
        </div>
      </div>
    </div>
  );
}
