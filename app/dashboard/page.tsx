"use client";

import Link from "next/link";
import { useStore } from "@/lib/store";
import { openWhatsAppChat, shareDonationWhatsApp } from "@/lib/whatsapp";
import { 
  Heart, 
  PlusCircle, 
  Search, 
  MessageSquare, 
  ShieldCheck, 
  Clock, 
  MapPin, 
  CheckCircle2,
  Users,
  Utensils,
  Share2
} from "lucide-react";

export default function DashboardPage() {
  const { currentUser, stats, donations, requests } = useStore();

  const myDonations = donations.filter((d) => d.donor_id === currentUser?.id || currentUser?.is_admin);
  const myRequests = requests.filter((r) => r.requester_id === currentUser?.id || currentUser?.is_admin);

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-10">
      {/* Welcome Card */}
      <div className="bg-white rounded-2xl border border-slate-200 p-6 sm:p-8 shadow-sm flex flex-col md:flex-row justify-between items-start md:items-center gap-6">
        <div className="space-y-1">
          <div className="flex items-center gap-2">
            <h1 className="text-2xl sm:text-3xl font-bold text-slate-900">
              Welcome, {currentUser ? currentUser.full_name : "Community Member"}!
            </h1>
            {currentUser?.is_admin && (
              <span className="px-2.5 py-0.5 rounded-full bg-orange-100 text-orange-800 text-xs font-bold">
                Admin
              </span>
            )}
          </div>
          <p className="text-xs text-slate-500">
            Account Role: <span className="font-semibold text-slate-700">{currentUser?.user_type || "Guest"}</span> • {currentUser?.email || "Signed in"}
          </p>
        </div>

        <div className="flex flex-wrap items-center gap-3">
          <Link
            href="/donate"
            className="inline-flex items-center gap-1.5 px-4 py-2.5 rounded-xl bg-green-600 hover:bg-green-700 text-white text-xs font-semibold shadow-sm transition-colors"
          >
            <PlusCircle className="w-4 h-4" /> Donate Food
          </Link>
          <Link
            href="/request"
            className="inline-flex items-center gap-1.5 px-4 py-2.5 rounded-xl bg-orange-600 hover:bg-orange-700 text-white text-xs font-semibold shadow-sm transition-colors"
          >
            Request Food
          </Link>
          <Link
            href="/available-food"
            className="inline-flex items-center gap-1.5 px-4 py-2.5 rounded-xl bg-slate-100 hover:bg-slate-200 text-slate-700 text-xs font-semibold transition-colors"
          >
            <Search className="w-4 h-4" /> Browse
          </Link>
          {currentUser?.is_admin && (
            <Link
              href="/admin"
              className="inline-flex items-center gap-1.5 px-4 py-2.5 rounded-xl bg-orange-50 hover:bg-orange-100 text-orange-700 border border-orange-200 text-xs font-semibold transition-colors"
            >
              <ShieldCheck className="w-4 h-4" /> Admin Portal
            </Link>
          )}
        </div>
      </div>

      {/* Metrics Row */}
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm">
          <span className="text-xs font-medium text-slate-500">Live Donations</span>
          <div className="text-2xl sm:text-3xl font-bold text-green-700 mt-1">{stats.totalDonations}</div>
          <span className="text-[11px] text-green-600 font-medium">{stats.activeDonations} active today</span>
        </div>

        <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm">
          <span className="text-xs font-medium text-slate-500">Food Requests</span>
          <div className="text-2xl sm:text-3xl font-bold text-orange-700 mt-1">{stats.totalRequests}</div>
          <span className="text-[11px] text-orange-600 font-medium">Community shelters</span>
        </div>

        <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm">
          <span className="text-xs font-medium text-slate-500">Completed Deliveries</span>
          <div className="text-2xl sm:text-3xl font-bold text-blue-700 mt-1">{stats.completedDonations}</div>
          <span className="text-[11px] text-blue-600 font-medium">Verified distributed</span>
        </div>

        <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm">
          <span className="text-xs font-medium text-slate-500">Meals Shared</span>
          <div className="text-2xl sm:text-3xl font-bold text-rose-700 mt-1">{stats.mealsShared}+</div>
          <span className="text-[11px] text-rose-600 font-medium">Lives touched</span>
        </div>
      </div>

      {/* Active Listings Section */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        {/* Managed Donations */}
        <div className="bg-white rounded-2xl border border-slate-200 shadow-sm p-6 space-y-4">
          <div className="flex justify-between items-center">
            <h3 className="font-bold text-slate-900 text-lg">My Food Donations ({myDonations.length})</h3>
            <Link href="/donate" className="text-xs font-semibold text-green-600 hover:underline">
              + New Donation
            </Link>
          </div>

          {myDonations.length === 0 ? (
            <p className="text-xs text-slate-500 py-6 text-center">No donations listed under your account yet.</p>
          ) : (
            <div className="space-y-3">
              {myDonations.map((d) => (
                <div key={d.id} className="p-4 rounded-xl border border-slate-100 bg-slate-50 space-y-2">
                  <div className="flex justify-between items-start">
                    <div>
                      <span className="text-[11px] font-bold text-green-700">{d.donation_id}</span>
                      <h4 className="font-semibold text-slate-900 text-sm">{d.food_name}</h4>
                    </div>
                    <span className="text-[10px] font-bold px-2 py-0.5 rounded-full bg-emerald-100 text-emerald-800">
                      {d.status}
                    </span>
                  </div>
                  <div className="text-xs text-slate-500">
                    Quantity: {d.quantity} (~{d.people_served} servings) • Safe until: {d.expiry_date}
                  </div>
                  <div className="flex gap-2 pt-1">
                    <button
                      onClick={() => shareDonationWhatsApp(d)}
                      className="px-2.5 py-1 bg-emerald-50 text-emerald-700 border border-emerald-200 text-xs rounded-lg flex items-center gap-1 font-medium"
                    >
                      <Share2 className="w-3.5 h-3.5" /> WhatsApp
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Managed Requests */}
        <div className="bg-white rounded-2xl border border-slate-200 shadow-sm p-6 space-y-4">
          <div className="flex justify-between items-center">
            <h3 className="font-bold text-slate-900 text-lg">My Food Requests ({myRequests.length})</h3>
            <Link href="/request" className="text-xs font-semibold text-orange-600 hover:underline">
              + New Request
            </Link>
          </div>

          {myRequests.length === 0 ? (
            <p className="text-xs text-slate-500 py-6 text-center">No food requests submitted under your account yet.</p>
          ) : (
            <div className="space-y-3">
              {myRequests.map((r) => (
                <div key={r.id} className="p-4 rounded-xl border border-slate-100 bg-slate-50 space-y-2">
                  <div className="flex justify-between items-start">
                    <div>
                      <span className="text-[11px] font-bold text-orange-700">{r.request_id}</span>
                      <h4 className="font-semibold text-slate-900 text-sm">{r.category}</h4>
                    </div>
                    <span className="text-[10px] font-bold px-2 py-0.5 rounded-full bg-blue-100 text-blue-800">
                      {r.status}
                    </span>
                  </div>
                  <div className="text-xs text-slate-500">
                    Needed for {r.people_count} people ({r.quantity}) by {r.required_date}
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
