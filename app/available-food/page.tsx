"use client";

import { useState } from "react";
import Link from "next/link";
import { useStore } from "@/lib/store";
import { shareDonationWhatsApp, openWhatsAppChat } from "@/lib/whatsapp";
import { 
  Search, 
  MapPin, 
  Clock, 
  Users, 
  Share2, 
  CheckCircle2, 
  AlertCircle,
  MessageSquare
} from "lucide-react";

export default function AvailableFoodPage() {
  const { donations, currentUser } = useStore();
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedCategory, setSelectedCategory] = useState("All");
  const [selectedType, setSelectedType] = useState("All");
  const [requestSuccess, setRequestSuccess] = useState<string | null>(null);

  const categories = ["All", "Rice", "Bakery Items", "Biriyani", "Fruits", "Meals", "Snacks"];

  const filtered = donations.filter((d) => {
    const matchesSearch =
      d.food_name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      d.city.toLowerCase().includes(searchTerm.toLowerCase()) ||
      d.pickup_address.toLowerCase().includes(searchTerm.toLowerCase()) ||
      d.donor_name.toLowerCase().includes(searchTerm.toLowerCase());

    const matchesCategory =
      selectedCategory === "All" || d.category.toLowerCase() === selectedCategory.toLowerCase();

    const matchesType =
      selectedType === "All" || d.food_type.toLowerCase() === selectedType.toLowerCase();

    return matchesSearch && matchesCategory && matchesType;
  });

  const handleRequestFood = (donation: any) => {
    setRequestSuccess(`Request sent for ${donation.food_name} (${donation.donation_id})! The donor and volunteer coordinators have been alerted.`);
    setTimeout(() => setRequestSuccess(null), 5000);
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
      <div>
        <h1 className="text-3xl font-bold text-slate-900">Available Surplus Food</h1>
        <p className="text-sm text-slate-500 mt-1">
          Browse verified fresh surplus food available in your community for immediate pickup.
        </p>
      </div>

      {requestSuccess && (
        <div className="p-4 bg-green-50 border border-green-200 text-green-800 text-sm rounded-xl flex items-center gap-2">
          <CheckCircle2 className="w-5 h-5 text-green-600 shrink-0" />
          <span>{requestSuccess}</span>
        </div>
      )}

      {/* Search and Filters Bar */}
      <div className="bg-white p-4 sm:p-6 rounded-2xl border border-slate-200 shadow-sm space-y-4">
        <div className="relative">
          <Search className="w-5 h-5 text-slate-400 absolute left-3.5 top-3.5" />
          <input
            type="text"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            placeholder="Search by food name, city, location, or donor..."
            className="w-full pl-11 pr-4 py-2.5 rounded-xl border border-slate-300 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
          />
        </div>

        <div className="flex flex-wrap items-center justify-between gap-4 pt-2">
          {/* Categories */}
          <div className="flex flex-wrap items-center gap-2">
            <span className="text-xs font-semibold text-slate-500 mr-1">Category:</span>
            {categories.map((cat) => (
              <button
                key={cat}
                onClick={() => setSelectedCategory(cat)}
                className={`px-3 py-1.5 rounded-lg text-xs font-medium transition-colors ${
                  selectedCategory === cat
                    ? "bg-green-600 text-white"
                    : "bg-slate-100 text-slate-600 hover:bg-slate-200"
                }`}
              >
                {cat}
              </button>
            ))}
          </div>

          {/* Veg / Non-Veg */}
          <div className="flex items-center gap-2">
            <span className="text-xs font-semibold text-slate-500 mr-1">Diet:</span>
            {["All", "Vegetarian", "Non-Vegetarian"].map((type) => (
              <button
                key={type}
                onClick={() => setSelectedType(type)}
                className={`px-3 py-1.5 rounded-lg text-xs font-medium transition-colors ${
                  selectedType === type
                    ? "bg-slate-900 text-white"
                    : "bg-slate-100 text-slate-600 hover:bg-slate-200"
                }`}
              >
                {type}
              </button>
            ))}
          </div>
        </div>
      </div>

      {/* Food Listings Grid */}
      {filtered.length === 0 ? (
        <div className="text-center py-16 bg-white rounded-2xl border border-slate-200">
          <p className="text-slate-500 text-sm">No food donations match your current search filters.</p>
          <button
            onClick={() => { setSearchTerm(""); setSelectedCategory("All"); setSelectedType("All"); }}
            className="mt-3 text-xs font-semibold text-green-600 hover:underline"
          >
            Reset Filters
          </button>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filtered.map((d) => (
            <div
              key={d.id}
              className="bg-white rounded-2xl border border-slate-200 shadow-sm p-6 flex flex-col justify-between hover:shadow-md transition-shadow"
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

                <h3 className="font-bold text-slate-900 text-lg mb-1">{d.food_name}</h3>
                <p className="text-xs text-slate-500 mb-3">Donor: <span className="font-medium text-slate-700">{d.donor_name}</span></p>

                {d.description && (
                  <p className="text-xs text-slate-600 mb-4 line-clamp-2 italic">
                    &ldquo;{d.description}&rdquo;
                  </p>
                )}

                <div className="space-y-2 text-xs text-slate-600 bg-slate-50 p-3.5 rounded-xl border border-slate-100 mb-4">
                  <div className="flex items-center justify-between">
                    <span className="text-slate-500 flex items-center gap-1.5"><Users className="w-3.5 h-3.5" /> Servings:</span>
                    <span className="font-bold text-green-700">~{d.people_served} people ({d.quantity})</span>
                  </div>
                  <div className="flex items-center justify-between">
                    <span className="text-slate-500 flex items-center gap-1.5"><Clock className="w-3.5 h-3.5" /> Safe Until:</span>
                    <span className="font-semibold text-orange-600">{d.expiry_date}</span>
                  </div>
                  <div className="flex items-center gap-1 text-slate-500 pt-1 border-t border-slate-200">
                    <MapPin className="w-3.5 h-3.5 text-slate-400 shrink-0" />
                    <span className="truncate">{d.pickup_address}, {d.city}</span>
                  </div>
                </div>
              </div>

              <div className="space-y-2 pt-2 border-t border-slate-100">
                <div className="flex gap-2">
                  <button
                    onClick={() => handleRequestFood(d)}
                    className="flex-1 py-2.5 text-center text-xs font-semibold text-white bg-green-600 hover:bg-green-700 rounded-xl transition-colors shadow-sm"
                  >
                    Request This Food
                  </button>

                  <button
                    onClick={() => shareDonationWhatsApp(d)}
                    className="p-2.5 text-emerald-700 bg-emerald-50 hover:bg-emerald-100 border border-emerald-200 rounded-xl transition-colors"
                    title="Share on WhatsApp"
                  >
                    <Share2 className="w-4 h-4" />
                  </button>
                </div>

                <button
                  onClick={() => openWhatsAppChat(`Hello! I would like to coordinate pickup for Donation ID: ${d.donation_id} (${d.food_name}).`)}
                  className="w-full py-2 bg-slate-50 hover:bg-slate-100 text-emerald-700 border border-emerald-200 text-xs font-semibold rounded-xl flex items-center justify-center gap-1.5 transition-colors"
                >
                  <MessageSquare className="w-3.5 h-3.5 fill-emerald-600 text-white" />
                  <span>Reserve via WhatsApp</span>
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
