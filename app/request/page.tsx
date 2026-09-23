"use client";

import { useState } from "react";
import { useStore } from "@/lib/store";
import { shareRequestWhatsApp, openWhatsAppChat } from "@/lib/whatsapp";
import { 
  CookingPot, 
  CheckCircle2, 
  Share2, 
  MessageSquare, 
  ArrowRight,
  Clock,
  MapPin
} from "lucide-react";

export default function RequestFoodPage() {
  const { currentUser, requests, addRequest } = useStore();

  const [category, setCategory] = useState("Vegetable Food");
  const [quantity, setQuantity] = useState("");
  const [peopleCount, setPeopleCount] = useState("");
  const [requiredDate, setRequiredDate] = useState("Today");
  const [requiredTime, setRequiredTime] = useState("07:30 PM");
  const [location, setLocation] = useState(currentUser?.address || "Chennai");
  const [contactNumber, setContactNumber] = useState(currentUser?.phone || "");
  const [reason, setReason] = useState("");
  const [notes, setNotes] = useState("");

  const [loading, setLoading] = useState(false);
  const [createdRequest, setCreatedRequest] = useState<any | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!quantity || !peopleCount || !location || !contactNumber || !reason) {
      alert("Please fill in all mandatory fields.");
      return;
    }

    setLoading(true);
    const result = await addRequest({
      requester_id: currentUser?.id || "guest-requester",
      requester_name: currentUser?.full_name || "Community Food Seeker",
      category,
      quantity,
      people_count: parseInt(peopleCount) || 20,
      required_date: requiredDate,
      required_time: requiredTime,
      location,
      contact_number: contactNumber,
      reason,
      notes
    });
    setLoading(false);
    setCreatedRequest(result);
  };

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-12">
      <div className="bg-white rounded-2xl border border-slate-200 shadow-xl p-6 sm:p-10 space-y-8">
        <div>
          <h1 className="text-3xl font-bold text-slate-900">Request Food Support</h1>
          <p className="text-sm text-slate-500 mt-1">
            Shelters, orphanages, old age homes, and community kitchens can submit food assistance requests.
          </p>
        </div>

        {createdRequest ? (
          <div className="p-6 bg-orange-50 border border-orange-200 rounded-2xl space-y-4">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-full bg-orange-600 text-white flex items-center justify-center">
                <CheckCircle2 className="w-6 h-6" />
              </div>
              <div>
                <h3 className="text-lg font-bold text-orange-950">Food Request Submitted Successfully!</h3>
                <p className="text-xs text-orange-800">Request ID: <span className="font-mono font-bold">{createdRequest.request_id}</span></p>
              </div>
            </div>

            <p className="text-sm text-orange-900">
              Your request for <span className="font-semibold">{createdRequest.category} ({createdRequest.quantity})</span> is now visible to community donors and volunteer couriers.
            </p>

            <div className="flex flex-wrap gap-3 pt-2">
              <button
                onClick={() => shareRequestWhatsApp(createdRequest)}
                className="px-4 py-2.5 bg-emerald-600 hover:bg-emerald-700 text-white font-semibold text-xs rounded-xl flex items-center gap-2 shadow-sm transition-colors"
              >
                <Share2 className="w-4 h-4" /> Share on WhatsApp
              </button>

              <button
                onClick={() => openWhatsAppChat(`Hello MealBridge! Urgent follow-up for Request ID: ${createdRequest.request_id} for ${createdRequest.people_count} people in ${createdRequest.location}.`)}
                className="px-4 py-2.5 bg-white text-emerald-800 border border-emerald-300 hover:bg-emerald-50 font-semibold text-xs rounded-xl flex items-center gap-2 transition-colors"
              >
                <MessageSquare className="w-4 h-4" /> Urgent Help on WhatsApp
              </button>

              <button
                onClick={() => {
                  setCreatedRequest(null);
                  setQuantity("");
                  setPeopleCount("");
                  setReason("");
                }}
                className="px-4 py-2.5 bg-slate-100 hover:bg-slate-200 text-slate-700 font-semibold text-xs rounded-xl transition-colors"
              >
                Submit Another Request
              </button>
            </div>
          </div>
        ) : (
          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">
                  Food Category *
                </label>
                <select
                  value={category}
                  onChange={(e) => setCategory(e.target.value)}
                  className="w-full px-3.5 py-2.5 rounded-xl border border-slate-300 text-sm focus:outline-none focus:ring-2 focus:ring-green-500 bg-white"
                >
                  <option value="Vegetable Food">Vegetarian Meals / Pulao</option>
                  <option value="Cooked Meals">Standard Cooked Meals</option>
                  <option value="Rice">Rice & Sambar / Dal</option>
                  <option value="Bakery Items">Breads & Bakery Supplies</option>
                  <option value="Breakfast">Breakfast & Tiffin</option>
                  <option value="Dry Groceries">Dry Rations / Rice Bags</option>
                </select>
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">
                  Number of People to Feed *
                </label>
                <input
                  type="number"
                  value={peopleCount}
                  onChange={(e) => setPeopleCount(e.target.value)}
                  placeholder="e.g. 40"
                  className="w-full px-3.5 py-2.5 rounded-xl border border-slate-300 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
                  required
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">
                  Estimated Quantity Needed *
                </label>
                <input
                  type="text"
                  value={quantity}
                  onChange={(e) => setQuantity(e.target.value)}
                  placeholder="e.g. 40 Packets or 20 kg"
                  className="w-full px-3.5 py-2.5 rounded-xl border border-slate-300 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
                  required
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">
                  Required By Date & Time *
                </label>
                <div className="grid grid-cols-2 gap-2">
                  <input
                    type="text"
                    value={requiredDate}
                    onChange={(e) => setRequiredDate(e.target.value)}
                    placeholder="Today"
                    className="w-full px-3.5 py-2.5 rounded-xl border border-slate-300 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
                  />
                  <input
                    type="text"
                    value={requiredTime}
                    onChange={(e) => setRequiredTime(e.target.value)}
                    placeholder="07:30 PM"
                    className="w-full px-3.5 py-2.5 rounded-xl border border-slate-300 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
                  />
                </div>
              </div>

              <div className="sm:col-span-2">
                <label className="block text-xs font-semibold text-slate-700 mb-1">
                  Delivery / Pickup Location *
                </label>
                <input
                  type="text"
                  value={location}
                  onChange={(e) => setLocation(e.target.value)}
                  placeholder="e.g. Hope Children Home, Kotturpuram, Chennai"
                  className="w-full px-3.5 py-2.5 rounded-xl border border-slate-300 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
                  required
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">
                  Contact Phone (WhatsApp) *
                </label>
                <input
                  type="tel"
                  value={contactNumber}
                  onChange={(e) => setContactNumber(e.target.value)}
                  placeholder="+91 9789012345"
                  className="w-full px-3.5 py-2.5 rounded-xl border border-slate-300 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
                  required
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">
                  Organization / Shelter Type *
                </label>
                <input
                  type="text"
                  value={reason}
                  onChange={(e) => setReason(e.target.value)}
                  placeholder="e.g. Evening dinner for 40 resident children"
                  className="w-full px-3.5 py-2.5 rounded-xl border border-slate-300 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
                  required
                />
              </div>

              <div className="sm:col-span-2">
                <label className="block text-xs font-semibold text-slate-700 mb-1">
                  Special Dietary or Packaging Notes
                </label>
                <textarea
                  rows={2}
                  value={notes}
                  onChange={(e) => setNotes(e.target.value)}
                  placeholder="e.g. Soft cooked food preferred for seniors; no eggs."
                  className="w-full px-3.5 py-2.5 rounded-xl border border-slate-300 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
                />
              </div>
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full py-3.5 bg-orange-600 hover:bg-orange-700 text-white font-semibold text-sm rounded-xl shadow-lg shadow-orange-600/20 transition-colors flex items-center justify-center gap-2"
            >
              {loading ? "Submitting Request..." : "Submit Community Food Request"}
              <ArrowRight className="w-4 h-4" />
            </button>
          </form>
        )}
      </div>

      {/* Active Community Food Requests List */}
      <div className="space-y-4">
        <h2 className="text-xl font-bold text-slate-900">Current Community Food Requests</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {requests.map((r) => (
            <div key={r.id} className="bg-white rounded-xl border border-slate-200 p-5 shadow-sm space-y-3">
              <div className="flex justify-between items-start">
                <div>
                  <span className="text-xs font-bold text-orange-700 bg-orange-50 px-2 py-0.5 rounded border border-orange-200">
                    {r.request_id}
                  </span>
                  <h4 className="font-bold text-slate-900 text-base mt-1.5">{r.category}</h4>
                  <p className="text-xs text-slate-500">By {r.requester_name}</p>
                </div>
                <span className="text-[11px] font-semibold text-blue-700 bg-blue-50 px-2 py-0.5 rounded">
                  {r.status}
                </span>
              </div>

              <p className="text-xs text-slate-600">{r.reason}</p>

              <div className="bg-slate-50 p-2.5 rounded-lg text-xs space-y-1 text-slate-600">
                <div className="flex justify-between">
                  <span className="text-slate-400">Needed for:</span>
                  <span className="font-semibold text-slate-800">{r.people_count} individuals ({r.quantity})</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-slate-400">Required:</span>
                  <span className="font-medium text-orange-600">{r.required_date} at {r.required_time}</span>
                </div>
                <div className="flex items-center gap-1 text-slate-500 pt-1 border-t border-slate-200">
                  <MapPin className="w-3.5 h-3.5 text-slate-400" />
                  <span className="truncate">{r.location}</span>
                </div>
              </div>

              <div className="flex gap-2 pt-1">
                <button
                  onClick={() => shareRequestWhatsApp(r)}
                  className="flex-1 py-2 text-center text-xs font-semibold text-emerald-800 bg-emerald-50 hover:bg-emerald-100 border border-emerald-200 rounded-lg transition-colors flex items-center justify-center gap-1.5"
                >
                  <Share2 className="w-3.5 h-3.5" /> Share on WhatsApp
                </button>

                <button
                  onClick={() => openWhatsAppChat(`Hello! I can supply food for Request ${r.request_id} (${r.category} for ${r.people_count} people).`)}
                  className="flex-1 py-2 text-center text-xs font-semibold text-white bg-green-600 hover:bg-green-700 rounded-lg transition-colors flex items-center justify-center gap-1.5"
                >
                  <MessageSquare className="w-3.5 h-3.5 fill-white" /> Fulfill via WhatsApp
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
