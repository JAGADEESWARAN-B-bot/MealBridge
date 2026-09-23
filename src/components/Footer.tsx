import Link from "next/link";
import { Heart } from "lucide-react";
import { MEALBRIDGE_DISPLAY_PHONE } from "@/lib/whatsapp";

export default function Footer() {
  return (
    <footer className="bg-slate-900 text-slate-400 py-12 border-t border-slate-800">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 grid grid-cols-1 md:grid-cols-4 gap-8">
        <div className="space-y-4">
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 rounded-lg bg-green-500 flex items-center justify-center text-white">
              <Heart className="w-4 h-4 fill-white" />
            </div>
            <span className="text-lg font-bold text-white tracking-tight">
              Meal<span className="text-green-500">Bridge</span>
            </span>
          </div>
          <p className="text-xs text-slate-400 leading-relaxed">
            Community Food Sharing Platform. Bridging hunger and food waste through real-time donor-to-seeker matching and WhatsApp coordination.
          </p>
          <div className="text-xs font-semibold text-green-400">
            &ldquo;Share Food. Reduce Waste. Help Someone Today.&rdquo;
          </div>
        </div>

        <div>
          <h4 className="text-white text-sm font-semibold mb-3">Quick Navigation</h4>
          <ul className="space-y-2 text-xs">
            <li><Link href="/" className="hover:text-white transition-colors">Home</Link></li>
            <li><Link href="/available-food" className="hover:text-white transition-colors">Available Food Nearby</Link></li>
            <li><Link href="/donate" className="hover:text-white transition-colors">Donate Surplus Food</Link></li>
            <li><Link href="/request" className="hover:text-white transition-colors">Submit Food Request</Link></li>
            <li><Link href="/dashboard" className="hover:text-white transition-colors">Community Dashboard</Link></li>
          </ul>
        </div>

        <div>
          <h4 className="text-white text-sm font-semibold mb-3">Community & Roles</h4>
          <ul className="space-y-2 text-xs">
            <li><Link href="/about" className="hover:text-white transition-colors">About MealBridge & SDGs</Link></li>
            <li><Link href="/register" className="hover:text-white transition-colors">Join as Food Donor</Link></li>
            <li><Link href="/register" className="hover:text-white transition-colors">Shelter & Seeker Access</Link></li>
            <li><Link href="/register" className="hover:text-white transition-colors">Volunteer Courier Network</Link></li>
            <li><Link href="/admin" className="hover:text-white transition-colors">Admin Verification Portal</Link></li>
          </ul>
        </div>

        <div>
          <h4 className="text-white text-sm font-semibold mb-3">Emergency & WhatsApp</h4>
          <p className="text-xs text-slate-400 mb-2">
            Official WhatsApp Assistance:
          </p>
          <div className="text-sm font-bold text-emerald-400 mb-3">
            {MEALBRIDGE_DISPLAY_PHONE}
          </div>
          <Link
            href="/contact"
            className="inline-block px-4 py-2 bg-slate-800 hover:bg-slate-700 text-white rounded-lg text-xs font-medium transition-colors"
          >
            Contact Volunteer Support
          </Link>
          <p className="text-[11px] text-slate-500 mt-3">
            B.Sc Computer Science (AI & Data Science) Capstone Project.
          </p>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 mt-8 pt-8 border-t border-slate-800 text-center text-xs text-slate-500">
        &copy; {new Date().getFullYear()} MealBridge. All rights reserved. Real-time PostgreSQL / Supabase sync enabled.
      </div>
    </footer>
  );
}
