"use client";

import Link from "next/link";
import { useStore } from "@/lib/store";
import { openWhatsAppChat, shareDonationWhatsApp, MEALBRIDGE_DISPLAY_PHONE } from "@/lib/whatsapp";
import { 
  Heart, 
  Utensils, 
  Users, 
  CheckCircle2, 
  Clock, 
  ArrowRight, 
  PlusCircle, 
  Search, 
  MessageSquare, 
  ShieldCheck, 
  Sparkles,
  MapPin,
  Share2
} from "lucide-react";

export default function HomePage() {
  const { stats, donations, requests } = useStore();
  const recentDonations = donations.slice(0, 3);

  return (
    <div className="space-y-16 pb-16">
      {/* Hero Section */}
      <section className="relative overflow-hidden bg-gradient-to-b from-green-50/80 via-white to-slate-50 pt-16 pb-20 border-b border-slate-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center max-w-3xl mx-auto space-y-6">
            <div className="inline-flex items-center gap-2 px-3.5 py-1.5 rounded-full bg-green-100 text-green-800 text-xs font-semibold tracking-wide uppercase">
              <Sparkles className="w-3.5 h-3.5 text-green-700" />
              Community Food Redistribution Platform
            </div>

            <h1 className="text-4xl sm:text-5xl lg:text-6xl font-extrabold text-slate-900 tracking-tight leading-tight">
              Share Food. Reduce Waste.{" "}
              <span className="text-green-600 block sm:inline">Help Someone Today.</span>
            </h1>

            <p className="text-lg sm:text-xl text-slate-600 leading-relaxed font-normal">
              MealBridge connects surplus food from marriages, restaurants, bakeries, and households 
              directly to nearby shelters, orphanages, and community kitchens in real time.
            </p>

            {/* Main Action Buttons */}
            <div className="flex flex-wrap justify-center gap-4 pt-4">
              <Link
                href="/donate"
                className="inline-flex items-center gap-2 px-6 py-3.5 rounded-xl bg-green-600 hover:bg-green-700 text-white font-semibold text-base shadow-lg shadow-green-600/20 transition-all duration-200 hover:-translate-y-0.5"
              >
                <PlusCircle className="w-5 h-5" />
                Donate Surplus Food
              </Link>

              <Link
                href="/available-food"
                className="inline-flex items-center gap-2 px-6 py-3.5 rounded-xl bg-white hover:bg-slate-50 text-slate-800 font-semibold text-base border border-slate-300 shadow-sm transition-all duration-200 hover:-translate-y-0.5"
              >
                <Search className="w-5 h-5 text-green-600" />
                Find Available Food
              </Link>

              <button
                onClick={() => openWhatsAppChat("Hello MealBridge Support! I need emergency food assistance.")}
                className="inline-flex items-center gap-2 px-6 py-3.5 rounded-xl bg-emerald-500 hover:bg-emerald-600 text-white font-semibold text-base shadow-lg shadow-emerald-500/20 transition-all duration-200 hover:-translate-y-0.5"
              >
                <MessageSquare className="w-5 h-5 fill-white" />
                WhatsApp Assistance
              </button>
            </div>

            <div className="pt-2 text-xs text-slate-500 flex items-center justify-center gap-3">
              <span className="flex items-center gap-1"><ShieldCheck className="w-4 h-4 text-green-600" /> Verified Donors</span>
              <span>•</span>
              <span className="flex items-center gap-1"><Clock className="w-4 h-4 text-orange-600" /> Real-time Matching</span>
              <span>•</span>
              <span className="flex items-center gap-1"><Heart className="w-4 h-4 text-rose-500 fill-rose-500" /> 100% Free Service</span>
            </div>
          </div>
        </div>
      </section>

      {/* Live Impact Statistics */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 -mt-8 sm:-mt-12 relative z-10">
        <div className="bg-white rounded-2xl shadow-xl border border-slate-200 p-6 sm:p-8">
          <div className="text-center mb-6">
            <h2 className="text-xl font-bold text-slate-900">Live Community Impact (Permanent Cloud Database)</h2>
            <p className="text-xs text-slate-500">Real-time statistics synchronized with PostgreSQL backend</p>
          </div>

          <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-4 text-center">
            <div className="p-4 rounded-xl bg-slate-50 border border-slate-100">
              <div className="text-2xl sm:text-3xl font-bold text-slate-900">{stats.totalUsers}</div>
              <div className="text-xs font-medium text-slate-500 mt-1">Total Users</div>
            </div>

            <div className="p-4 rounded-xl bg-green-50 border border-green-100">
              <div className="text-2xl sm:text-3xl font-bold text-green-700">{stats.totalDonations}</div>
              <div className="text-xs font-medium text-green-700 mt-1">Donations Listed</div>
            </div>

            <div className="p-4 rounded-xl bg-orange-50 border border-orange-100">
              <div className="text-2xl sm:text-3xl font-bold text-orange-700">{stats.totalRequests}</div>
              <div className="text-xs font-medium text-orange-700 mt-1">Food Requests</div>
            </div>

            <div className="p-4 rounded-xl bg-emerald-50 border border-emerald-100">
              <div className="text-2xl sm:text-3xl font-bold text-emerald-700">{stats.activeDonations}</div>
              <div className="text-xs font-medium text-emerald-700 mt-1">Active Now</div>
            </div>

            <div className="p-4 rounded-xl bg-blue-50 border border-blue-100">
              <div className="text-2xl sm:text-3xl font-bold text-blue-700">{stats.completedDonations}</div>
              <div className="text-xs font-medium text-blue-700 mt-1">Delivered</div>
            </div>

            <div className="p-4 rounded-xl bg-rose-50 border border-rose-100">
              <div className="text-2xl sm:text-3xl font-bold text-rose-700">{stats.mealsShared}+</div>
              <div className="text-xs font-medium text-rose-700 mt-1">Meals Shared</div>
            </div>
          </div>
        </div>
      </section>

      {/* How MealBridge Works */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="text-center max-w-2xl mx-auto mb-12">
          <h2 className="text-3xl font-bold text-slate-900">How MealBridge Works</h2>
          <p className="text-slate-600 text-sm mt-2">
            A seamless bridge connecting donors, community organizations, and volunteer dispatchers.
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm relative">
            <div className="w-12 h-12 rounded-xl bg-green-100 text-green-700 font-bold text-lg flex items-center justify-center mb-4">
              1
            </div>
            <h3 className="text-lg font-bold text-slate-900 mb-2">1. Donors List Surplus Food</h3>
            <p className="text-sm text-slate-600 leading-relaxed">
              Catering halls, restaurants, or individuals publish surplus food details including quantity, 
              servings, expiry window, and pickup location.
            </p>
          </div>

          <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm relative">
            <div className="w-12 h-12 rounded-xl bg-orange-100 text-orange-700 font-bold text-lg flex items-center justify-center mb-4">
              2
            </div>
            <h3 className="text-lg font-bold text-slate-900 mb-2">2. Seekers & Shelters Request</h3>
            <p className="text-sm text-slate-600 leading-relaxed">
              Verified orphanages, old-age homes, and community volunteers view active meals or post instant food requirements.
            </p>
          </div>

          <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm relative">
            <div className="w-12 h-12 rounded-xl bg-blue-100 text-blue-700 font-bold text-lg flex items-center justify-center mb-4">
              3
            </div>
            <h3 className="text-lg font-bold text-slate-900 mb-2">3. Real-Time WhatsApp Pickup</h3>
            <p className="text-sm text-slate-600 leading-relaxed">
              Coordinates instant drop-off and pickup through our dedicated WhatsApp hotline ({MEALBRIDGE_DISPLAY_PHONE}).
            </p>
          </div>
        </div>
      </section>

      {/* Available Food Right Now */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 mb-8">
          <div>
            <h2 className="text-2xl font-bold text-slate-900">Available Surplus Food Nearby</h2>
            <p className="text-xs text-slate-500">Fresh listings ready for immediate pickup and distribution</p>
          </div>
          <Link
            href="/available-food"
            className="inline-flex items-center gap-1.5 text-sm font-semibold text-green-600 hover:text-green-700"
          >
            View All Available Meals <ArrowRight className="w-4 h-4" />
          </Link>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {recentDonations.map((d) => (
            <div
              key={d.id}
              className="bg-white rounded-2xl border border-slate-200 shadow-sm p-5 hover:shadow-md transition-shadow flex flex-col justify-between"
            >
              <div>
                <div className="flex justify-between items-start mb-3">
                  <span className="text-xs font-bold text-green-700 bg-green-50 px-2.5 py-1 rounded-md border border-green-200">
                    {d.donation_id}
                  </span>
                  <span className={`text-[11px] font-semibold px-2 py-0.5 rounded-full ${
                    d.food_type === "Vegetarian" ? "bg-emerald-100 text-emerald-800" : "bg-red-100 text-red-800"
                  }`}>
                    {d.food_type}
                  </span>
                </div>

                <h3 className="font-bold text-slate-900 text-base mb-1">{d.food_name}</h3>
                <p className="text-xs text-slate-500 mb-3">By {d.donor_name}</p>

                <div className="space-y-1.5 text-xs text-slate-600 mb-4 bg-slate-50 p-3 rounded-xl border border-slate-100">
                  <div className="flex items-center justify-between">
                    <span className="text-slate-500">Quantity:</span>
                    <span className="font-semibold text-slate-800">{d.quantity}</span>
                  </div>
                  <div className="flex items-center justify-between">
                    <span className="text-slate-500">Servings:</span>
                    <span className="font-semibold text-green-700">~{d.people_served} people</span>
                  </div>
                  <div className="flex items-center justify-between">
                    <span className="text-slate-500">Safe Until:</span>
                    <span className="font-medium text-orange-600">{d.expiry_date}</span>
                  </div>
                  <div className="flex items-center gap-1 text-slate-500 pt-1 border-t border-slate-200">
                    <MapPin className="w-3.5 h-3.5 text-slate-400" />
                    <span className="truncate">{d.pickup_address}, {d.city}</span>
                  </div>
                </div>
              </div>

              <div className="flex gap-2 pt-2 border-t border-slate-100">
                <Link
                  href="/available-food"
                  className="flex-1 py-2 text-center text-xs font-semibold text-white bg-green-600 hover:bg-green-700 rounded-lg transition-colors"
                >
                  Request This Food
                </Link>
                <button
                  onClick={() => shareDonationWhatsApp(d)}
                  className="p-2 text-emerald-700 bg-emerald-50 hover:bg-emerald-100 border border-emerald-200 rounded-lg transition-colors"
                  title="Share on WhatsApp"
                >
                  <Share2 className="w-4 h-4" />
                </button>
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* WhatsApp Community Callout Banner */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="rounded-2xl bg-gradient-to-r from-emerald-600 to-green-700 text-white p-8 sm:p-10 shadow-xl flex flex-col md:flex-row justify-between items-center gap-6">
          <div className="space-y-2 max-w-xl text-center md:text-left">
            <div className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full bg-emerald-500/40 text-xs font-semibold text-emerald-100">
              <MessageSquare className="w-3.5 h-3.5" />
              Official WhatsApp Emergency Coordination
            </div>
            <h3 className="text-2xl sm:text-3xl font-bold">Have large surplus food to donate right now?</h3>
            <p className="text-emerald-100 text-sm">
              Connect immediately with the MealBridge Community Coordinator on WhatsApp ({MEALBRIDGE_DISPLAY_PHONE}).
            </p>
          </div>

          <button
            onClick={() => openWhatsAppChat("URGENT FOOD DONATION: We have fresh surplus meals ready for immediate pickup. Please dispatch a courier or connect us with a nearby shelter.")}
            className="px-6 py-3.5 bg-white text-emerald-800 hover:bg-emerald-50 rounded-xl font-bold text-sm shadow-md transition-all whitespace-nowrap"
          >
            Chat with Coordinator on WhatsApp
          </button>
        </div>
      </section>
    </div>
  );
}
