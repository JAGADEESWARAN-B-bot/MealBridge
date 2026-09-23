"use client";

import { useStore } from "@/lib/store";
import { Bell, CheckCheck, Clock } from "lucide-react";

export default function NotificationsPage() {
  const { notifications, markNotificationRead, markAllNotificationsRead } = useStore();

  return (
    <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-slate-900">Notifications</h1>
          <p className="text-xs text-slate-500 mt-1">Updates on food donations, verification, and community requests.</p>
        </div>

        {notifications.some((n) => !n.is_read) && (
          <button
            onClick={markAllNotificationsRead}
            className="flex items-center gap-1.5 px-3 py-1.5 text-xs font-semibold text-green-700 bg-green-50 hover:bg-green-100 rounded-lg transition-colors border border-green-200"
          >
            <CheckCheck className="w-3.5 h-3.5" /> Mark All as Read
          </button>
        )}
      </div>

      {notifications.length === 0 ? (
        <div className="bg-white rounded-2xl border border-slate-200 p-12 text-center text-slate-500 text-sm">
          No notifications to show.
        </div>
      ) : (
        <div className="space-y-3">
          {notifications.map((n) => (
            <div
              key={n.id}
              onClick={() => markNotificationRead(n.id)}
              className={`p-4 rounded-xl border transition-all cursor-pointer ${
                n.is_read
                  ? "bg-white border-slate-200 text-slate-600"
                  : "bg-green-50/60 border-green-200 shadow-sm"
              }`}
            >
              <div className="flex justify-between items-start gap-4">
                <div className="space-y-1">
                  <div className="flex items-center gap-2">
                    <span className="font-bold text-slate-900 text-sm">{n.title}</span>
                    {!n.is_read && (
                      <span className="w-2 h-2 rounded-full bg-green-600"></span>
                    )}
                  </div>
                  <p className="text-xs text-slate-600 leading-relaxed">{n.message}</p>
                </div>
                <div className="text-[10px] text-slate-400 flex items-center gap-1 shrink-0">
                  <Clock className="w-3 h-3" />
                  <span>{new Date(n.created_at || Date.now()).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</span>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
