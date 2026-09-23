import { createClient } from '@supabase/supabase-js';

const supabaseUrl = process.env.NEXT_PUBLIC_SUPABASE_URL || 'https://demo-mealbridge.supabase.co';
const supabaseAnonKey = process.env.NEXT_PUBLIC_SUPABASE_ANON_KEY || 'demo-anon-key';

export const supabase = createClient(supabaseUrl, supabaseAnonKey);

export interface UserProfile {
  id: string;
  full_name: string;
  email: string;
  phone: string;
  user_type: string;
  address: string;
  status: string;
  is_admin: boolean;
  created_at?: string;
}

export interface FoodDonation {
  id: string;
  donation_id: string;
  donor_id: string;
  donor_name: string;
  food_name: string;
  category: string;
  quantity: string;
  people_served: number;
  food_type: string;
  preparation_date: string;
  expiry_date: string;
  pickup_address: string;
  city: string;
  contact_number: string;
  description: string;
  image_url?: string;
  status: string;
  created_at?: string;
}

export interface FoodRequest {
  id: string;
  request_id: string;
  requester_id: string;
  requester_name: string;
  category: string;
  quantity: string;
  people_count: number;
  required_date: string;
  required_time: string;
  location: string;
  contact_number: string;
  reason: string;
  notes?: string;
  status: string;
  created_at?: string;
}

export interface AppNotification {
  id: string;
  user_id: string;
  title: string;
  message: string;
  is_read: boolean;
  created_at?: string;
}

export interface ContactMessage {
  id: string;
  name: string;
  email: string;
  phone: string;
  subject: string;
  message: string;
  created_at?: string;
}

// Initial seed data for permanent state and instant browsing
export const INITIAL_DONATIONS: FoodDonation[] = [
  {
    id: "don-uuid-1",
    donation_id: "DON-1001",
    donor_id: "donor-1",
    donor_name: "Grand Palace Catering",
    food_name: "Fresh Vegetable Pulao & Dal",
    category: "Rice",
    quantity: "25 kg (Approx 60 boxes)",
    people_served: 60,
    food_type: "Vegetarian",
    preparation_date: "Today, 12:30 PM",
    expiry_date: "Today, 10:30 PM",
    pickup_address: "No. 42 Anna Salai, T. Nagar",
    city: "Chennai",
    contact_number: "+91 9840123456",
    description: "Surplus wedding lunch packages prepared under strict hygienic conditions. Packed in foil boxes.",
    status: "Active",
    created_at: new Date().toISOString()
  },
  {
    id: "don-uuid-2",
    donation_id: "DON-1002",
    donor_id: "donor-2",
    donor_name: "Anand Bakery & Sweets",
    food_name: "Wheat Bread Loaves & Vegetable Puffs",
    category: "Bakery Items",
    quantity: "45 Packs",
    people_served: 45,
    food_type: "Vegetarian",
    preparation_date: "Today, 08:00 AM",
    expiry_date: "Tomorrow, 08:00 PM",
    pickup_address: "14 Gandhi Road, Velachery",
    city: "Chennai",
    contact_number: "+91 9840234567",
    description: "Freshly baked whole wheat breads and veggie puffs from today morning batch.",
    status: "Active",
    created_at: new Date().toISOString()
  },
  {
    id: "don-uuid-3",
    donation_id: "DON-1003",
    donor_id: "donor-3",
    donor_name: "Spice Symphony Restaurant",
    food_name: "Chicken Biriyani & Raita",
    category: "Biriyani",
    quantity: "35 Meal Packs",
    people_served: 35,
    food_type: "Non-Vegetarian",
    preparation_date: "Today, 01:15 PM",
    expiry_date: "Today, 09:00 PM",
    pickup_address: "88 Ring Road, Indiranagar",
    city: "Bangalore",
    contact_number: "+91 9840345678",
    description: "Fresh party buffet surplus packaged neatly in hygienic meal trays.",
    status: "Active",
    created_at: new Date().toISOString()
  },
  {
    id: "don-uuid-4",
    donation_id: "DON-1004",
    donor_id: "donor-4",
    donor_name: "Green Apple Supermarket",
    food_name: "Fresh Seasonal Fruits Crate",
    category: "Fruits",
    quantity: "20 kg Apples & Oranges",
    people_served: 40,
    food_type: "Vegetarian",
    preparation_date: "Today, 10:00 AM",
    expiry_date: "In 2 Days",
    pickup_address: "Shop 5, Express Avenue, Royapettah",
    city: "Chennai",
    contact_number: "+91 9840456789",
    description: "Crates of washed fresh fruit suitable for children or elder care centers.",
    status: "Active",
    created_at: new Date().toISOString()
  }
];

export const INITIAL_REQUESTS: FoodRequest[] = [
  {
    id: "req-uuid-1",
    request_id: "REQ-2001",
    requester_id: "seeker-1",
    requester_name: "Hope Children Home",
    category: "Rice",
    quantity: "30-40 Meals",
    people_count: 35,
    required_date: "Today",
    required_time: "07:30 PM",
    location: "Kotturpuram, Chennai",
    contact_number: "+91 9789012345",
    reason: "Evening dinner for resident children at shelter.",
    notes: "Prefer mild spiced meals.",
    status: "Pending",
    created_at: new Date().toISOString()
  },
  {
    id: "req-uuid-2",
    request_id: "REQ-2002",
    requester_id: "seeker-2",
    requester_name: "Karuna Old Age Welfare Society",
    category: "Vegetable Food",
    quantity: "25 Meals",
    people_count: 25,
    required_date: "Tomorrow",
    required_time: "12:30 PM",
    location: "Adyar, Chennai",
    contact_number: "+91 9789123456",
    reason: "Lunch support for senior residents.",
    notes: "Soft cooked vegetarian diet preferred.",
    status: "Approved",
    created_at: new Date().toISOString()
  }
];
