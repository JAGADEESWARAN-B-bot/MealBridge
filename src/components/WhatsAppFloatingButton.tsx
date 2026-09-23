"use client";

import { openWhatsAppChat, MEALBRIDGE_DISPLAY_PHONE } from "@/lib/whatsapp";
import { MessageSquare } from "lucide-react";

export default function WhatsAppFloatingButton() {
  return (
    <button
      onClick={() => openWhatsAppChat("Hello MealBridge Support Team! I have an inquiry or need assistance.")}
      className="fixed bottom-6 right-6 z-40 flex items-center gap-2 bg-[#25D366] hover:bg-[#20ba59] text-white px-4 py-3 rounded-full shadow-lg hover:shadow-xl transition-all duration-200 group font-semibold text-sm"
      title={`Chat on WhatsApp (${MEALBRIDGE_DISPLAY_PHONE})`}
    >
      <MessageSquare className="w-5 h-5 fill-white" />
      <span className="hidden sm:inline">WhatsApp Support</span>
    </button>
  );
}
