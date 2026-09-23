export const MEALBRIDGE_WHATSAPP_NUMBER = "917548813430";
export const MEALBRIDGE_DISPLAY_PHONE = "+91 7548813430";

export function openWhatsAppChat(message: string, phone: string = MEALBRIDGE_WHATSAPP_NUMBER) {
  const cleanPhone = phone.replace(/\D/g, "");
  const encoded = encodeURIComponent(message);
  const url = `https://wa.me/${cleanPhone}?text=${encoded}`;
  if (typeof window !== "undefined") {
    window.open(url, "_blank");
  }
}

export function shareDonationWhatsApp(donation: {
  donation_id: string;
  food_name: string;
  quantity: string;
  people_served: number;
  food_type: string;
  city: string;
  expiry_date: string;
}) {
  const msg = `🌱 *MealBridge Food Donation Alert!*\n\n` +
    `*Food:* ${donation.food_name} (${donation.food_type})\n` +
    `*Donation ID:* ${donation.donation_id}\n` +
    `*Quantity:* ${donation.quantity} (Serves ${donation.people_served} people)\n` +
    `*Location:* ${donation.city}\n` +
    `*Safe Until:* ${donation.expiry_date}\n\n` +
    `Join MealBridge or chat with community support on WhatsApp (${MEALBRIDGE_DISPLAY_PHONE}) to request this food.`;
  openWhatsAppChat(msg);
}

export function shareRequestWhatsApp(request: {
  request_id: string;
  category: string;
  quantity: string;
  people_count: number;
  location: string;
  required_date: string;
  required_time: string;
  reason: string;
}) {
  const msg = `🚨 *MealBridge Urgent Food Request!*\n\n` +
    `*Request ID:* ${request.request_id}\n` +
    `*Category:* ${request.category}\n` +
    `*Quantity:* ${request.quantity} (For ${request.people_count} individuals)\n` +
    `*Location:* ${request.location}\n` +
    `*Required By:* ${request.required_date} at ${request.required_time}\n` +
    `*Purpose:* ${request.reason}\n\n` +
    `Can your home, hotel, or kitchen help bridge this meal? Contact MealBridge Support (${MEALBRIDGE_DISPLAY_PHONE}).`;
  openWhatsAppChat(msg);
}
