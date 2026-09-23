"use client";

import { useState } from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useStore } from "@/lib/store";
import { shareDonationWhatsApp, openWhatsAppChat } from "@/lib/whatsapp";
import { 
  PlusCircle, 
  CheckCircle2, 
  AlertCircle, 
  Share2, 
  MessageSquare,
  ArrowRight
} from "lucide-react";

export default function DonateFoodPage() {
  const { currentUser, addDonation } = useStore();
  const router = useRouter();

  const [foodName, setFoodName] = useState("");
  const [category, setCategory] = useState("Meals");
  const [quantity, setQuantity] = useState("");
  const [peopleServed, setPeopleServed] = useState("");
  const [foodType, setFoodType] = useState("Vegetarian");
  const [prepTime, setPrepTime] = useState("Today, 12:00 PM");
  const [expiryTime, setExpiryTime] = useState("Today, 09:00 PM");
  const [address, setAddress] = useState(currentUser?.address || "");
  const [city, setCity] = useState("Chennai");
  const [phone, setPhone] = useState(currentUser?.phone || "");
  const [notes, setNotes] = useState("");

  const [loading, setLoading] = useState(false);
  const [createdDonation, setCreatedDonation] = useState<any | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!foodName || !quantity || !peopleServed || !address || !phone) {
      alert("Please fill in all mandatory fields.");
      return;
    }

    setLoading(true);
    const result = await addDonation({
      donor_id: currentUser?.id || "guest-donor",
      donor_name: currentUser?.full_name || "Community Food Donor",
      food_name: foodName,
      category,
      quantity,
      people_served: parseInt(peopleServed) || 10,
      food_type: foodType,
      preparation_date: prepTime,
      expiry_date: expiryTime,
      pickup_address: address,
      city,
      contact_number: phone,
      description: notes
    });
    setLoading(false);
    setCreatedDonation(result);
  };

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
      <div className="bg-white rounded-2xl border border-slate-200 shadow-xl p-6 sm:p-10 space-y-8">
        <div>
          <div className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full bg-green-100 text-green-800 text-xs font-semibold mb-3">
            <PlusCircle className="w-3.5 h-3.5 text-green-700" />
            Food Redistribution Form
          </div>
          <h1 className="text-3xl font-bold text-slate-900">Donate Surplus Food</h1>
          <p className="text-sm text-slate-500 mt-1">
            Provide details of clean, edible surplus food to connect with local shelters and volunteer dispatchers.
          </p>
        </div>

        {createdDonation ? (
          <div className="p-6 bg-green-50 border border-green-200 rounded-2xl space-y-4">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-full bg-green-600 text-white flex items-center justify-center">
                <CheckCircle2 className="w-6 h-6" />
              </div>
              <div>
                <h3 className="text-lg font-bold text-green-900">Food Donation Listed Successfully!</h3>
                <p className="text-xs text-green-700">Donation ID: <span className="font-mono font-bold">{createdDonation.donation_id}</span></p>
              </div>
            </div>

            <p className="text-sm text-green-800">
              Your donation of <span className="font-semibold">{createdDonation.food_name}</span> has been stored permanently in the database.
            </p>

            <div className="flex flex-wrap gap-3 pt-2">
              <button
                onClick={() => shareDonationWhatsApp(createdDonation)}
                className="px-4 py-2.5 bg-emerald-600 hover:bg-emerald-700 text-white font-semibold text-xs rounded-xl flex items-center gap-2 shadow-sm transition-colors"
              >
                <Share2 className="w-4 h-4" /> Share on WhatsApp
              </button>

              <button
                onClick={() => openWhatsAppChat(`Hello MealBridge! I just posted donation ${createdDonation.donation_id} (${createdDonation.food_name}). Please dispatch pickup.`)}
                className="px-4 py-2.5 bg-white text-emerald-800 border border-emerald-300 hover:bg-emerald-50 font-semibold text-xs rounded-xl flex items-center gap-2 transition-colors"
              >
                <MessageSquare className="w-4 h-4" /> Chat with Coordinator
              </button>

              <button
                onClick={() => {
                  setCreatedDonation(null);
                  setFoodName("");
                  setQuantity("");
                  setPeopleServed("");
                }}
                className="px-4 py-2.5 bg-slate-100 hover:bg-slate-200 text-slate-700 font-semibold text-xs rounded-xl transition-colors"
              >
                Donate Another Item
              </button>
            </div>
          </div>
        ) : (
          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div className="sm:col-span-2">
                <label className="block text-xs font-semibold text-slate-700 mb-1">
                  Food Name / Description *
                </label>
                <input
                  type="text"
                  value={foodName}
                  onChange={(e) => setFoodName(e.target.value)}
                  placeholder="e.g. Fresh Vegetable Biriyani with Raita"
                  className="w-full px-3.5 py-2.5 rounded-xl border border-slate-300 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
                  required
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">
                  Category *
                </label>
                <select
                  value={category}
                  onChange={(e) => setCategory(e.target.value)}
                  className="w-full px-3.5 py-2.5 rounded-xl border border-slate-300 text-sm focus:outline-none focus:ring-2 focus:ring-green-500 bg-white"
                >
                  <option value="Meals">Cooked Meals / Buffet</option>
                  <option value="Rice">Rice Dishes & Pulao</option>
                  <option value="Bakery Items">Bakery & Breads</option>
                  <option value="Biriyani">Biriyani</option>
                  <option value="Fruits">Fresh Fruits</option>
                  <option value="Snacks">Snacks & Packaged Food</option>
                </select>
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">
                  Dietary Classification *
                </label>
                <div className="flex gap-4 pt-1.5">
                  <label className="flex items-center gap-2 text-xs font-medium text-slate-700 cursor-pointer">
                    <input
                      type="radio"
                      name="diet"
                      value="Vegetarian"
                      checked={foodType === "Vegetarian"}
                      onChange={() => setFoodType("Vegetarian")}
                      className="text-green-600 focus:ring-green-500"
                    />
                    Vegetarian
                  </label>
                  <label className="flex items-center gap-2 text-xs font-medium text-slate-700 cursor-pointer">
                    <input
                      type="radio"
                      name="diet"
                      value="Non-Vegetarian"
                      checked={foodType === "Non-Vegetarian"}
                      onChange={() => setFoodType("Non-Vegetarian")}
                      className="text-red-600 focus:ring-red-500"
                    />
                    Non-Vegetarian
                  </label>
                </div>
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">
                  Quantity / Packets *
                </label>
                <input
                  type="text"
                  value={quantity}
                  onChange={(e) => setQuantity(e.target.value)}
                  placeholder="e.g. 30 Food Trays or 15 kg"
                  className="w-full px-3.5 py-2.5 rounded-xl border border-slate-300 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
                  required
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">
                  Estimated People Served *
                </label>
                <input
                  type="number"
                  value={peopleServed}
                  onChange={(e) => setPeopleServed(e.target.value)}
                  placeholder="e.g. 50"
                  className="w-full px-3.5 py-2.5 rounded-xl border border-slate-300 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
                  required
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">
                  Preparation Time *
                </label>
                <input
                  type="text"
                  value={prepTime}
                  onChange={(e) => setPrepTime(e.target.value)}
                  placeholder="e.g. Today, 01:00 PM"
                  className="w-full px-3.5 py-2.5 rounded-xl border border-slate-300 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">
                  Safe Expiry Window *
                </label>
                <input
                  type="text"
                  value={expiryTime}
                  onChange={(e) => setExpiryTime(e.target.value)}
                  placeholder="e.g. Today, 10:00 PM"
                  className="w-full px-3.5 py-2.5 rounded-xl border border-slate-300 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
                  required
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">
                  City *
                </label>
                <input
                  type="text"
                  value={city}
                  onChange={(e) => setCity(e.target.value)}
                  placeholder="e.g. Chennai"
                  className="w-full px-3.5 py-2.5 rounded-xl border border-slate-300 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
                  required
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">
                  Donor Contact Phone (WhatsApp) *
                </label>
                <input
                  type="tel"
                  value={phone}
                  onChange={(e) => setPhone(e.target.value)}
                  placeholder="+91 9840123456"
                  className="w-full px-3.5 py-2.5 rounded-xl border border-slate-300 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
                  required
                />
              </div>

              <div className="sm:col-span-2">
                <label className="block text-xs font-semibold text-slate-700 mb-1">
                  Detailed Pickup Address *
                </label>
                <textarea
                  rows={2}
                  value={address}
                  onChange={(e) => setAddress(e.target.value)}
                  placeholder="Street, Landmark, Building name..."
                  className="w-full px-3.5 py-2.5 rounded-xl border border-slate-300 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
                  required
                />
              </div>

              <div className="sm:col-span-2">
                <label className="block text-xs font-semibold text-slate-700 mb-1">
                  Hygiene & Packaging Notes
                </label>
                <textarea
                  rows={2}
                  value={notes}
                  onChange={(e) => setNotes(e.target.value)}
                  placeholder="e.g. Packed in disposable foil boxes. Kept refrigerated."
                  className="w-full px-3.5 py-2.5 rounded-xl border border-slate-300 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
                />
              </div>
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full py-3.5 bg-green-600 hover:bg-green-700 text-white font-semibold text-sm rounded-xl shadow-lg shadow-green-600/20 transition-colors flex items-center justify-center gap-2"
            >
              {loading ? "Publishing Donation..." : "Publish Food Donation (Permanent Storage)"}
              <ArrowRight className="w-4 h-4" />
            </button>
          </form>
        )}
      </div>
    </div>
  );
}
