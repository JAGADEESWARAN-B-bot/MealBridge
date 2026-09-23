"use client";

import { useState } from "react";
import { useStore } from "@/lib/store";
import { 
  ShieldCheck, 
  Users, 
  Utensils, 
  CookingPot, 
  Trash2, 
  Check, 
  X, 
  AlertTriangle 
} from "lucide-react";

export default function AdminPage() {
  const { 
    currentUser, 
    stats, 
    donations, 
    requests, 
    users, 
    updateDonationStatus, 
    deleteDonation, 
    updateRequestStatus, 
    toggleUserStatus 
  } = useStore();

  const [activeTab, setActiveTab] = useState<"donations" | "requests" | "users">("donations");

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
      <div className="bg-white rounded-2xl border border-slate-200 p-6 sm:p-8 shadow-sm flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div>
          <div className="flex items-center gap-2">
            <div className="p-2 bg-orange-100 text-orange-700 rounded-xl">
              <ShieldCheck className="w-6 h-6" />
            </div>
            <div>
              <h1 className="text-2xl font-bold text-slate-900">MealBridge Admin Portal</h1>
              <p className="text-xs text-slate-500">Moderation and verification controls for community food distribution.</p>
            </div>
          </div>
        </div>

        <div className="flex gap-2">
          <button
            onClick={() => setActiveTab("donations")}
            className={`px-3 py-1.5 rounded-lg text-xs font-semibold transition-colors ${
              activeTab === "donations" ? "bg-green-600 text-white" : "bg-slate-100 text-slate-600 hover:bg-slate-200"
            }`}
          >
            Donations ({donations.length})
          </button>
          <button
            onClick={() => setActiveTab("requests")}
            className={`px-3 py-1.5 rounded-lg text-xs font-semibold transition-colors ${
              activeTab === "requests" ? "bg-orange-600 text-white" : "bg-slate-100 text-slate-600 hover:bg-slate-200"
            }`}
          >
            Requests ({requests.length})
          </button>
          <button
            onClick={() => setActiveTab("users")}
            className={`px-3 py-1.5 rounded-lg text-xs font-semibold transition-colors ${
              activeTab === "users" ? "bg-blue-600 text-white" : "bg-slate-100 text-slate-600 hover:bg-slate-200"
            }`}
          >
            Users ({users.length})
          </button>
        </div>
      </div>

      {/* Admin Stats Grid */}
      <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-6 gap-4">
        <div className="bg-white p-4 rounded-xl border border-slate-200 text-center">
          <div className="text-xs text-slate-500 font-medium">Total Registered</div>
          <div className="text-2xl font-bold text-slate-900 mt-1">{users.length}</div>
        </div>
        <div className="bg-white p-4 rounded-xl border border-slate-200 text-center">
          <div className="text-xs text-slate-500 font-medium">All Donations</div>
          <div className="text-2xl font-bold text-green-700 mt-1">{donations.length}</div>
        </div>
        <div className="bg-white p-4 rounded-xl border border-slate-200 text-center">
          <div className="text-xs text-slate-500 font-medium">All Requests</div>
          <div className="text-2xl font-bold text-orange-700 mt-1">{requests.length}</div>
        </div>
        <div className="bg-white p-4 rounded-xl border border-slate-200 text-center">
          <div className="text-xs text-slate-500 font-medium">Active Status</div>
          <div className="text-2xl font-bold text-emerald-700 mt-1">{stats.activeDonations}</div>
        </div>
        <div className="bg-white p-4 rounded-xl border border-slate-200 text-center">
          <div className="text-xs text-slate-500 font-medium">Completed</div>
          <div className="text-2xl font-bold text-blue-700 mt-1">{stats.completedDonations}</div>
        </div>
        <div className="bg-white p-4 rounded-xl border border-slate-200 text-center">
          <div className="text-xs text-slate-500 font-medium">Total Meals</div>
          <div className="text-2xl font-bold text-rose-700 mt-1">{stats.mealsShared}+</div>
        </div>
      </div>

      {/* Tab: Donations */}
      {activeTab === "donations" && (
        <div className="bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden">
          <div className="p-4 sm:p-6 border-b border-slate-100 flex justify-between items-center">
            <h3 className="font-bold text-slate-900 text-base">Manage Community Food Donations</h3>
            <span className="text-xs text-slate-500">Approve, Complete, or Remove listings</span>
          </div>

          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs text-slate-600">
              <thead className="bg-slate-50 text-slate-700 uppercase font-semibold border-b border-slate-200">
                <tr>
                  <th className="p-3.5">ID / Food</th>
                  <th className="p-3.5">Donor</th>
                  <th className="p-3.5">Servings / Quantity</th>
                  <th className="p-3.5">Location</th>
                  <th className="p-3.5">Status</th>
                  <th className="p-3.5 text-right">Moderation Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {donations.map((d) => (
                  <tr key={d.id} className="hover:bg-slate-50/50">
                    <td className="p-3.5">
                      <div className="font-mono text-green-700 font-bold">{d.donation_id}</div>
                      <div className="font-semibold text-slate-900">{d.food_name}</div>
                    </td>
                    <td className="p-3.5">{d.donor_name}</td>
                    <td className="p-3.5">{d.people_served} people ({d.quantity})</td>
                    <td className="p-3.5">{d.city}</td>
                    <td className="p-3.5">
                      <span className="px-2 py-0.5 rounded-full text-[10px] font-bold bg-slate-100 text-slate-800">
                        {d.status}
                      </span>
                    </td>
                    <td className="p-3.5 text-right space-x-1">
                      <button
                        onClick={() => updateDonationStatus(d.id, "Approved")}
                        className="px-2 py-1 bg-green-50 text-green-700 hover:bg-green-100 rounded font-medium text-[11px]"
                      >
                        Approve
                      </button>
                      <button
                        onClick={() => updateDonationStatus(d.id, "Completed")}
                        className="px-2 py-1 bg-blue-50 text-blue-700 hover:bg-blue-100 rounded font-medium text-[11px]"
                      >
                        Complete
                      </button>
                      <button
                        onClick={() => deleteDonation(d.id)}
                        className="p-1 text-red-600 hover:bg-red-50 rounded"
                        title="Delete Donation"
                      >
                        <Trash2 className="w-3.5 h-3.5" />
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {/* Tab: Requests */}
      {activeTab === "requests" && (
        <div className="bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden">
          <div className="p-4 sm:p-6 border-b border-slate-100 flex justify-between items-center">
            <h3 className="font-bold text-slate-900 text-base">Manage Community Food Requests</h3>
            <span className="text-xs text-slate-500">Verify shelter needs</span>
          </div>

          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs text-slate-600">
              <thead className="bg-slate-50 text-slate-700 uppercase font-semibold border-b border-slate-200">
                <tr>
                  <th className="p-3.5">ID / Category</th>
                  <th className="p-3.5">Requester</th>
                  <th className="p-3.5">People Count</th>
                  <th className="p-3.5">Required By</th>
                  <th className="p-3.5">Status</th>
                  <th className="p-3.5 text-right">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {requests.map((r) => (
                  <tr key={r.id} className="hover:bg-slate-50/50">
                    <td className="p-3.5">
                      <div className="font-mono text-orange-700 font-bold">{r.request_id}</div>
                      <div className="font-semibold text-slate-900">{r.category}</div>
                    </td>
                    <td className="p-3.5">{r.requester_name}</td>
                    <td className="p-3.5">{r.people_count} individuals</td>
                    <td className="p-3.5">{r.required_date} at {r.required_time}</td>
                    <td className="p-3.5">
                      <span className="px-2 py-0.5 rounded-full text-[10px] font-bold bg-slate-100 text-slate-800">
                        {r.status}
                      </span>
                    </td>
                    <td className="p-3.5 text-right space-x-1">
                      <button
                        onClick={() => updateRequestStatus(r.id, "Approved")}
                        className="px-2 py-1 bg-green-50 text-green-700 hover:bg-green-100 rounded font-medium text-[11px]"
                      >
                        Approve
                      </button>
                      <button
                        onClick={() => updateRequestStatus(r.id, "Completed")}
                        className="px-2 py-1 bg-blue-50 text-blue-700 hover:bg-blue-100 rounded font-medium text-[11px]"
                      >
                        Complete
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {/* Tab: Users */}
      {activeTab === "users" && (
        <div className="bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden">
          <div className="p-4 sm:p-6 border-b border-slate-100 flex justify-between items-center">
            <h3 className="font-bold text-slate-900 text-base">Registered Users & Organizations</h3>
            <span className="text-xs text-slate-500">Toggle activation status</span>
          </div>

          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs text-slate-600">
              <thead className="bg-slate-50 text-slate-700 uppercase font-semibold border-b border-slate-200">
                <tr>
                  <th className="p-3.5">Name / Email</th>
                  <th className="p-3.5">Role</th>
                  <th className="p-3.5">Phone</th>
                  <th className="p-3.5">Status</th>
                  <th className="p-3.5 text-right">Action</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {users.map((u) => (
                  <tr key={u.id} className="hover:bg-slate-50/50">
                    <td className="p-3.5">
                      <div className="font-semibold text-slate-900">{u.full_name}</div>
                      <div className="text-[11px] text-slate-500">{u.email}</div>
                    </td>
                    <td className="p-3.5">
                      <span className="px-2 py-0.5 rounded text-[10px] font-semibold bg-slate-100 text-slate-700">
                        {u.user_type}
                      </span>
                    </td>
                    <td className="p-3.5">{u.phone}</td>
                    <td className="p-3.5">
                      <span className={`px-2 py-0.5 rounded-full text-[10px] font-bold ${
                        u.status === "Active" ? "bg-green-100 text-green-800" : "bg-red-100 text-red-800"
                      }`}>
                        {u.status}
                      </span>
                    </td>
                    <td className="p-3.5 text-right">
                      <button
                        onClick={() => toggleUserStatus(u.id)}
                        className={`px-3 py-1 rounded text-[11px] font-semibold transition-colors ${
                          u.status === "Active"
                            ? "bg-red-50 text-red-700 hover:bg-red-100 border border-red-200"
                            : "bg-green-50 text-green-700 hover:bg-green-100 border border-green-200"
                        }`}
                      >
                        {u.status === "Active" ? "Deactivate" : "Activate"}
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
}
