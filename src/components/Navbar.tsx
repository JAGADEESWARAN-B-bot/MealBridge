"use client";

import Link from "next/link";
import { useState } from "react";
import { useStore } from "@/lib/store";
import { MEALBRIDGE_DISPLAY_PHONE, openWhatsAppChat } from "@/lib/whatsapp";
import { 
  Heart, 
  Menu, 
  X, 
  Bell, 
  User, 
  ShieldCheck, 
  PlusCircle, 
  Search, 
  MessageSquare
} from "lucide-react";

export default function Navbar() {
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
  const { currentUser, notifications } = useStore();

  const unreadCount = notifications.filter((n) => !n.is_read).length;

  return (
    <header className="sticky top-0 z-50 bg-white border-b border-slate-200 shadow-sm">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          {/* Logo & Brand */}
          <Link href="/" className="flex items-center gap-2.5">
            <div className="w-10 h-10 rounded-xl bg-green-600 flex items-center justify-center text-white shadow-md">
              <Heart className="w-5 h-5 fill-white" />
            </div>
            <div>
              <span className="text-xl font-bold text-slate-900 tracking-tight flex items-center gap-1.5">
                Meal<span className="text-green-600">Bridge</span>
              </span>
              <span className="block text-[10px] text-slate-500 font-medium -mt-1 uppercase tracking-wider">
                Community Food Sharing
              </span>
            </div>
          </Link>

          {/* Desktop Navigation */}
          <nav className="hidden md:flex items-center gap-6 text-sm font-medium text-slate-600">
            <Link href="/" className="hover:text-green-600 transition-colors">
              Home
            </Link>
            <Link href="/available-food" className="hover:text-green-600 transition-colors flex items-center gap-1">
              <Search className="w-4 h-4" /> Available Food
            </Link>
            <Link href="/donate" className="hover:text-green-600 transition-colors flex items-center gap-1 text-green-700 font-semibold">
              <PlusCircle className="w-4 h-4" /> Donate Food
            </Link>
            <Link href="/request" className="hover:text-green-600 transition-colors">
              Request Food
            </Link>
            <Link href="/dashboard" className="hover:text-green-600 transition-colors">
              Dashboard
            </Link>
            <Link href="/about" className="hover:text-green-600 transition-colors">
              About
            </Link>
            <Link href="/contact" className="hover:text-green-600 transition-colors">
              Contact
            </Link>
          </nav>

          {/* Right Action Icons */}
          <div className="hidden md:flex items-center gap-3">
            {/* WhatsApp Quick Link */}
            <button
              onClick={() => openWhatsAppChat("Hello MealBridge Support Team! I have an inquiry regarding food sharing.")}
              className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-full bg-emerald-50 text-emerald-700 text-xs font-semibold hover:bg-emerald-100 transition-colors border border-emerald-200"
              title="Official WhatsApp: +91 7548813430"
            >
              <MessageSquare className="w-3.5 h-3.5 fill-emerald-600 text-white" />
              <span>WhatsApp</span>
            </button>

            {/* Notifications */}
            <Link
              href="/notifications"
              className="relative p-2 text-slate-600 hover:text-slate-900 rounded-lg hover:bg-slate-100 transition-colors"
              title="Notifications"
            >
              <Bell className="w-5 h-5" />
              {unreadCount > 0 && (
                <span className="absolute top-1.5 right-1.5 w-4 h-4 rounded-full bg-orange-600 text-white text-[10px] font-bold flex items-center justify-center">
                  {unreadCount}
                </span>
              )}
            </Link>

            {/* Admin Badge */}
            {currentUser?.is_admin && (
              <Link
                href="/admin"
                className="flex items-center gap-1 px-2.5 py-1 text-xs font-semibold text-orange-700 bg-orange-50 border border-orange-200 rounded-lg hover:bg-orange-100"
              >
                <ShieldCheck className="w-3.5 h-3.5" />
                Admin
              </Link>
            )}

            {/* Profile or Login */}
            {currentUser ? (
              <Link
                href="/profile"
                className="flex items-center gap-2 px-3 py-1.5 text-xs font-medium text-slate-700 bg-slate-100 rounded-lg hover:bg-slate-200"
              >
                <User className="w-4 h-4 text-green-700" />
                <span className="max-w-[120px] truncate">{currentUser.full_name}</span>
              </Link>
            ) : (
              <div className="flex items-center gap-2">
                <Link
                  href="/login"
                  className="px-3 py-1.5 text-xs font-medium text-slate-700 hover:text-slate-900"
                >
                  Log in
                </Link>
                <Link
                  href="/register"
                  className="px-3 py-1.5 text-xs font-medium text-white bg-green-600 rounded-lg hover:bg-green-700 shadow-sm"
                >
                  Register
                </Link>
              </div>
            )}
          </div>

          {/* Mobile hamburger */}
          <div className="flex md:hidden items-center gap-2">
            <button
              onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
              className="p-2 text-slate-600 hover:text-slate-900"
            >
              {mobileMenuOpen ? <X className="w-6 h-6" /> : <Menu className="w-6 h-6" />}
            </button>
          </div>
        </div>

        {/* Mobile menu dropdown */}
        {mobileMenuOpen && (
          <div className="md:hidden py-4 border-t border-slate-100 space-y-2">
            <Link
              href="/"
              onClick={() => setMobileMenuOpen(false)}
              className="block px-3 py-2 rounded-md text-base font-medium text-slate-700 hover:bg-slate-50"
            >
              Home
            </Link>
            <Link
              href="/available-food"
              onClick={() => setMobileMenuOpen(false)}
              className="block px-3 py-2 rounded-md text-base font-medium text-slate-700 hover:bg-slate-50"
            >
              Available Food
            </Link>
            <Link
              href="/donate"
              onClick={() => setMobileMenuOpen(false)}
              className="block px-3 py-2 rounded-md text-base font-medium text-green-700 font-semibold hover:bg-green-50"
            >
              Donate Food
            </Link>
            <Link
              href="/request"
              onClick={() => setMobileMenuOpen(false)}
              className="block px-3 py-2 rounded-md text-base font-medium text-slate-700 hover:bg-slate-50"
            >
              Request Food
            </Link>
            <Link
              href="/dashboard"
              onClick={() => setMobileMenuOpen(false)}
              className="block px-3 py-2 rounded-md text-base font-medium text-slate-700 hover:bg-slate-50"
            >
              Dashboard
            </Link>
            <Link
              href="/about"
              onClick={() => setMobileMenuOpen(false)}
              className="block px-3 py-2 rounded-md text-base font-medium text-slate-700 hover:bg-slate-50"
            >
              About
            </Link>
            <Link
              href="/contact"
              onClick={() => setMobileMenuOpen(false)}
              className="block px-3 py-2 rounded-md text-base font-medium text-slate-700 hover:bg-slate-50"
            >
              Contact
            </Link>
            <Link
              href="/notifications"
              onClick={() => setMobileMenuOpen(false)}
              className="block px-3 py-2 rounded-md text-base font-medium text-slate-700 hover:bg-slate-50"
            >
              Notifications ({unreadCount})
            </Link>
            {currentUser?.is_admin && (
              <Link
                href="/admin"
                onClick={() => setMobileMenuOpen(false)}
                className="block px-3 py-2 rounded-md text-base font-medium text-orange-700 hover:bg-orange-50"
              >
                Admin Portal
              </Link>
            )}
            <div className="pt-2 border-t border-slate-100 flex items-center justify-between px-3">
              {currentUser ? (
                <Link
                  href="/profile"
                  onClick={() => setMobileMenuOpen(false)}
                  className="text-sm font-medium text-slate-800"
                >
                  Profile ({currentUser.full_name})
                </Link>
              ) : (
                <div className="flex gap-4">
                  <Link
                    href="/login"
                    onClick={() => setMobileMenuOpen(false)}
                    className="text-sm font-medium text-slate-700"
                  >
                    Log In
                  </Link>
                  <Link
                    href="/register"
                    onClick={() => setMobileMenuOpen(false)}
                    className="text-sm font-medium text-green-600 font-semibold"
                  >
                    Register
                  </Link>
                </div>
              )}
            </div>
          </div>
        )}
      </div>
    </header>
  );
}
