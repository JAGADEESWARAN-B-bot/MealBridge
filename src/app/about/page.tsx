"use client";

import { openWhatsAppChat, MEALBRIDGE_DISPLAY_PHONE } from "@/lib/whatsapp";
import { Heart, Globe, Award, Sparkles, MessageSquare, ShieldCheck, CheckCircle2 } from "lucide-react";

export default function AboutPage() {
  return (
    <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-12 space-y-12">
      <div className="text-center max-w-3xl mx-auto space-y-4">
        <div className="w-14 h-14 bg-green-100 text-green-700 rounded-2xl flex items-center justify-center mx-auto shadow-sm">
          <Heart className="w-8 h-8 fill-green-600 text-green-600" />
        </div>
        <h1 className="text-4xl font-extrabold text-slate-900 tracking-tight">About MealBridge</h1>
        <p className="text-base text-slate-600 font-medium">
          Community Food Sharing Platform — &ldquo;Share Food. Reduce Waste. Help Someone Today.&rdquo;
        </p>
      </div>

      <div className="bg-white rounded-2xl border border-slate-200 p-8 shadow-sm space-y-6">
        <h2 className="text-xl font-bold text-slate-900 flex items-center gap-2">
          <Award className="w-5 h-5 text-orange-600" /> Project Background & Academic Context
        </h2>
        <p className="text-sm text-slate-600 leading-relaxed">
          MealBridge is engineered as a final-year B.Sc Computer Science (AI & Data Science) capstone project. 
          Its core objective is eliminating food wastage by providing a real-time, zero-latency digital bridge 
          between food providers (wedding halls, convention centers, catering agencies, restaurants) and beneficiary organizations 
          (shelters, orphanages, destitute homes, daily-wage worker communities).
        </p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="bg-white rounded-2xl border border-slate-200 p-6 shadow-sm space-y-3">
          <h3 className="font-bold text-slate-900 text-base flex items-center gap-2">
            <Globe className="w-5 h-5 text-green-600" /> United Nations SDG 2: Zero Hunger
          </h3>
          <p className="text-xs text-slate-600 leading-relaxed">
            Millions of wholesome, freshly prepared meals are discarded daily following events. 
            MealBridge captures this surplus within the critical 3-to-6 hour freshness window and reroutes it directly to hunger hotspots.
          </p>
        </div>

        <div className="bg-white rounded-2xl border border-slate-200 p-6 shadow-sm space-y-3">
          <h3 className="font-bold text-slate-900 text-base flex items-center gap-2">
            <Sparkles className="w-5 h-5 text-orange-600" /> United Nations SDG 12: Responsible Consumption
          </h3>
          <p className="text-xs text-slate-600 leading-relaxed">
            Decomposing food waste in municipal landfills generates significant quantities of greenhouse methane gas. 
            By diverting edible food to human consumption, MealBridge supports environmental sustainability.
          </p>
        </div>
      </div>

      {/* WhatsApp Support Callout */}
      <div className="bg-emerald-50 border border-emerald-200 rounded-2xl p-6 sm:p-8 flex flex-col sm:flex-row justify-between items-center gap-4">
        <div>
          <h3 className="text-lg font-bold text-emerald-950">Official WhatsApp Dispatch Desk</h3>
          <p className="text-xs text-emerald-800 mt-1">
            Need to coordinate a bulk surplus food donation pickup or partner your organization?
          </p>
          <div className="text-sm font-mono font-bold text-emerald-900 mt-2">
            {MEALBRIDGE_DISPLAY_PHONE}
          </div>
        </div>

        <button
          onClick={() => openWhatsAppChat("Hello MealBridge Team! I would like to partner my organization with MealBridge.")}
          className="px-5 py-3 bg-emerald-600 hover:bg-emerald-700 text-white rounded-xl text-xs font-semibold shadow-sm flex items-center gap-2 transition-colors whitespace-nowrap"
        >
          <MessageSquare className="w-4 h-4 fill-white" /> Connect on WhatsApp
        </button>
      </div>
    </div>
  );
}
