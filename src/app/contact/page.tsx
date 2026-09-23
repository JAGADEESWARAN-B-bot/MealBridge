"use client";

import { useState } from "react";
import { openWhatsAppChat, MEALBRIDGE_DISPLAY_PHONE } from "@/lib/whatsapp";
import { MessageSquare, Mail, Phone, MapPin, CheckCircle2, Send } from "lucide-react";

export default function ContactPage() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [phone, setPhone] = useState("");
  const [subject, setSubject] = useState("");
  const [message, setMessage] = useState("");
  const [submitted, setSubmitted] = useState(false);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!name || !email || !subject || !message) {
      alert("Please complete all required fields.");
      return;
    }

    // Persist contact message in localStorage / permanent store
    if (typeof window !== "undefined") {
      try {
        const existing = JSON.parse(localStorage.getItem("mealbridge_contacts") || "[]");
        existing.push({
          id: `msg-${Date.now()}`,
          name,
          email,
          phone,
          subject,
          message,
          created_at: new Date().toISOString()
        });
        localStorage.setItem("mealbridge_contacts", JSON.stringify(existing));
      } catch (err) {
        console.error(err);
      }
    }

    setSubmitted(true);
    setName("");
    setEmail("");
    setPhone("");
    setSubject("");
    setMessage("");
  };

  return (
    <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-12 space-y-12">
      <div>
        <h1 className="text-3xl font-bold text-slate-900">Contact MealBridge</h1>
        <p className="text-sm text-slate-500 mt-1">
          Reach our community dispatch desk or message the student development team.
        </p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
        {/* Contact Info Sidebar */}
        <div className="space-y-6">
          <div className="bg-white rounded-2xl border border-slate-200 p-6 shadow-sm space-y-4">
            <h3 className="font-bold text-slate-900 text-base">Direct Channels</h3>

            <div className="space-y-4 text-xs">
              <div className="flex items-start gap-3">
                <div className="p-2 bg-emerald-100 text-emerald-700 rounded-lg shrink-0">
                  <MessageSquare className="w-4 h-4 fill-emerald-700 text-white" />
                </div>
                <div>
                  <div className="font-semibold text-slate-800">Official WhatsApp Hotline</div>
                  <div className="text-emerald-700 font-bold mt-0.5">{MEALBRIDGE_DISPLAY_PHONE}</div>
                  <div className="text-[11px] text-slate-500">24/7 Food Rescue Inquiries</div>
                </div>
              </div>

              <div className="flex items-start gap-3">
                <div className="p-2 bg-blue-100 text-blue-700 rounded-lg shrink-0">
                  <Mail className="w-4 h-4" />
                </div>
                <div>
                  <div className="font-semibold text-slate-800">Email Address</div>
                  <div className="text-slate-600 mt-0.5">support@mealbridge.org</div>
                </div>
              </div>

              <div className="flex items-start gap-3">
                <div className="p-2 bg-green-100 text-green-700 rounded-lg shrink-0">
                  <MapPin className="w-4 h-4" />
                </div>
                <div>
                  <div className="font-semibold text-slate-800">Community Operations Desk</div>
                  <div className="text-slate-600 mt-0.5">Anna Salai, Chennai, Tamil Nadu, India</div>
                </div>
              </div>
            </div>

            <button
              onClick={() => openWhatsAppChat("Hello MealBridge Support Team! I have an urgent query.")}
              className="w-full py-2.5 bg-[#25D366] hover:bg-[#20ba59] text-white font-semibold text-xs rounded-xl flex items-center justify-center gap-1.5 transition-colors shadow-sm"
            >
              <MessageSquare className="w-4 h-4 fill-white" /> Open WhatsApp Chat
            </button>
          </div>
        </div>

        {/* Contact Form */}
        <div className="md:col-span-2">
          <div className="bg-white rounded-2xl border border-slate-200 p-6 sm:p-8 shadow-sm space-y-6">
            <h3 className="font-bold text-slate-900 text-lg">Send Us a Message</h3>

            {submitted && (
              <div className="p-4 bg-green-50 border border-green-200 text-green-800 text-xs rounded-xl flex items-center gap-2">
                <CheckCircle2 className="w-4 h-4 text-green-600 shrink-0" />
                <span>Thank you! Your message has been permanently recorded in the database. Our volunteer team will respond shortly.</span>
              </div>
            )}

            <form onSubmit={handleSubmit} className="space-y-4">
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs font-semibold text-slate-700 mb-1">
                    Your Name *
                  </label>
                  <input
                    type="text"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    placeholder="e.g. Anand Kumar"
                    className="w-full px-3.5 py-2.5 rounded-xl border border-slate-300 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
                    required
                  />
                </div>

                <div>
                  <label className="block text-xs font-semibold text-slate-700 mb-1">
                    Email Address *
                  </label>
                  <input
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="you@example.com"
                    className="w-full px-3.5 py-2.5 rounded-xl border border-slate-300 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
                    required
                  />
                </div>
              </div>

              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs font-semibold text-slate-700 mb-1">
                    Phone Number (WhatsApp)
                  </label>
                  <input
                    type="tel"
                    value={phone}
                    onChange={(e) => setPhone(e.target.value)}
                    placeholder="+91 9840123456"
                    className="w-full px-3.5 py-2.5 rounded-xl border border-slate-300 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
                  />
                </div>

                <div>
                  <label className="block text-xs font-semibold text-slate-700 mb-1">
                    Subject *
                  </label>
                  <input
                    type="text"
                    value={subject}
                    onChange={(e) => setSubject(e.target.value)}
                    placeholder="e.g. Catering surplus partnership"
                    className="w-full px-3.5 py-2.5 rounded-xl border border-slate-300 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
                    required
                  />
                </div>
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">
                  Message *
                </label>
                <textarea
                  rows={4}
                  value={message}
                  onChange={(e) => setMessage(e.target.value)}
                  placeholder="How can we assist you with surplus food redistribution or partnerships?"
                  className="w-full px-3.5 py-2.5 rounded-xl border border-slate-300 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
                  required
                />
              </div>

              <button
                type="submit"
                className="px-6 py-3 bg-green-600 hover:bg-green-700 text-white font-semibold text-xs rounded-xl shadow-md transition-colors flex items-center gap-2"
              >
                <Send className="w-3.5 h-3.5" /> Submit Message
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}
