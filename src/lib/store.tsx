"use client";

import React, { createContext, useContext, useState, useEffect } from "react";
import {
  UserProfile,
  FoodDonation,
  FoodRequest,
  AppNotification,
  INITIAL_DONATIONS,
  INITIAL_REQUESTS,
  supabase
} from "./supabase";

interface StoreContextType {
  currentUser: UserProfile | null;
  donations: FoodDonation[];
  requests: FoodRequest[];
  notifications: AppNotification[];
  users: UserProfile[];
  login: (email: string) => Promise<{ success: boolean; message: string }>;
  register: (user: Omit<UserProfile, "id" | "status" | "is_admin">) => Promise<{ success: boolean; message: string }>;
  logout: () => void;
  updateProfile: (name: string, phone: string, address: string) => void;
  addDonation: (donation: Omit<FoodDonation, "id" | "donation_id" | "status" | "created_at">) => Promise<FoodDonation>;
  addRequest: (req: Omit<FoodRequest, "id" | "request_id" | "status" | "created_at">) => Promise<FoodRequest>;
  updateDonationStatus: (id: string, status: string) => void;
  updateRequestStatus: (id: string, status: string) => void;
  deleteDonation: (id: string) => void;
  toggleUserStatus: (id: string) => void;
  markNotificationRead: (id: string) => void;
  markAllNotificationsRead: () => void;
  stats: {
    totalUsers: number;
    totalDonations: number;
    totalRequests: number;
    activeDonations: number;
    completedDonations: number;
    mealsShared: number;
  };
}

const StoreContext = createContext<StoreContextType | null>(null);

const DEFAULT_USERS: UserProfile[] = [
  {
    id: "admin-1",
    full_name: "MealBridge Admin",
    email: "admin@mealbridge.org",
    phone: "+91 7548813430",
    user_type: "Admin",
    address: "Community Food Network HQ, Chennai",
    status: "Active",
    is_admin: true
  },
  {
    id: "donor-1",
    full_name: "Grand Palace Catering",
    email: "donor@mealbridge.org",
    phone: "+91 9840123456",
    user_type: "Donor",
    address: "T. Nagar, Chennai",
    status: "Active",
    is_admin: false
  },
  {
    id: "seeker-1",
    full_name: "Hope Children Home",
    email: "seeker@mealbridge.org",
    phone: "+91 9789012345",
    user_type: "Food Seeker",
    address: "Kotturpuram, Chennai",
    status: "Active",
    is_admin: false
  }
];

export function StoreProvider({ children }: { children: React.ReactNode }) {
  const [currentUser, setCurrentUser] = useState<UserProfile | null>(DEFAULT_USERS[0]);
  const [donations, setDonations] = useState<FoodDonation[]>(INITIAL_DONATIONS);
  const [requests, setRequests] = useState<FoodRequest[]>(INITIAL_REQUESTS);
  const [users, setUsers] = useState<UserProfile[]>(DEFAULT_USERS);
  const [notifications, setNotifications] = useState<AppNotification[]>([
    {
      id: "notif-1",
      user_id: "admin-1",
      title: "Welcome to MealBridge",
      message: "Permanent food sharing network is active. Real-time matching ready.",
      is_read: false,
      created_at: new Date().toISOString()
    }
  ]);

  // Load from localStorage or Supabase on mount
  useEffect(() => {
    if (typeof window !== "undefined") {
      try {
        const savedUser = localStorage.getItem("mealbridge_user");
        if (savedUser) setCurrentUser(JSON.parse(savedUser));

        const savedDonations = localStorage.getItem("mealbridge_donations");
        if (savedDonations) setDonations(JSON.parse(savedDonations));

        const savedRequests = localStorage.getItem("mealbridge_requests");
        if (savedRequests) setRequests(JSON.parse(savedRequests));

        const savedUsers = localStorage.getItem("mealbridge_users");
        if (savedUsers) setUsers(JSON.parse(savedUsers));

        const savedNotifs = localStorage.getItem("mealbridge_notifs");
        if (savedNotifs) setNotifications(JSON.parse(savedNotifs));
      } catch (e) {
        console.error("Local storage load error", e);
      }
    }
  }, []);

  // Sync to local storage
  const persist = (key: string, data: any) => {
    if (typeof window !== "undefined") {
      try {
        localStorage.setItem(key, JSON.stringify(data));
      } catch (e) {
        console.error("Storage error", e);
      }
    }
  };

  const login = async (email: string) => {
    const match = users.find((u) => u.email.toLowerCase() === email.trim().toLowerCase());
    if (match) {
      if (match.status === "Inactive") {
        return { success: false, message: "Account has been deactivated. Contact support on WhatsApp." };
      }
      setCurrentUser(match);
      persist("mealbridge_user", match);
      return { success: true, message: `Welcome back, ${match.full_name}!` };
    }
    return { success: false, message: "No account found with this email. Please register." };
  };

  const register = async (userData: Omit<UserProfile, "id" | "status" | "is_admin">) => {
    const existing = users.find((u) => u.email.toLowerCase() === userData.email.trim().toLowerCase());
    if (existing) {
      return { success: false, message: "Email is already registered. Please log in." };
    }

    const newUser: UserProfile = {
      ...userData,
      id: `user-${Date.now()}`,
      status: "Active",
      is_admin: userData.email.toLowerCase().includes("admin")
    };

    const updated = [newUser, ...users];
    setUsers(updated);
    persist("mealbridge_users", updated);
    setCurrentUser(newUser);
    persist("mealbridge_user", newUser);
    return { success: true, message: "Registration successful!" };
  };

  const logout = () => {
    setCurrentUser(null);
    if (typeof window !== "undefined") {
      localStorage.removeItem("mealbridge_user");
    }
  };

  const updateProfile = (name: string, phone: string, address: string) => {
    if (!currentUser) return;
    const updated = { ...currentUser, full_name: name, phone, address };
    setCurrentUser(updated);
    persist("mealbridge_user", updated);
    const updatedUsers = users.map((u) => (u.id === updated.id ? updated : u));
    setUsers(updatedUsers);
    persist("mealbridge_users", updatedUsers);
  };

  const addDonation = async (
    donationData: Omit<FoodDonation, "id" | "donation_id" | "status" | "created_at">
  ) => {
    const newDonation: FoodDonation = {
      ...donationData,
      id: `don-${Date.now()}`,
      donation_id: `DON-${1000 + donations.length + 1}`,
      status: "Active",
      created_at: new Date().toISOString()
    };

    const updated = [newDonation, ...donations];
    setDonations(updated);
    persist("mealbridge_donations", updated);

    // Push notification
    const newNotif: AppNotification = {
      id: `notif-${Date.now()}`,
      user_id: newDonation.donor_id,
      title: `Donation Created: ${newDonation.donation_id}`,
      message: `Your listing for ${newDonation.food_name} is now live and visible to food seekers.`,
      is_read: false,
      created_at: new Date().toISOString()
    };
    const updatedNotifs = [newNotif, ...notifications];
    setNotifications(updatedNotifs);
    persist("mealbridge_notifs", updatedNotifs);

    return newDonation;
  };

  const addRequest = async (
    reqData: Omit<FoodRequest, "id" | "request_id" | "status" | "created_at">
  ) => {
    const newReq: FoodRequest = {
      ...reqData,
      id: `req-${Date.now()}`,
      request_id: `REQ-${2000 + requests.length + 1}`,
      status: "Pending",
      created_at: new Date().toISOString()
    };

    const updated = [newReq, ...requests];
    setRequests(updated);
    persist("mealbridge_requests", updated);

    const newNotif: AppNotification = {
      id: `notif-${Date.now()}`,
      user_id: newReq.requester_id,
      title: `Food Request: ${newReq.request_id}`,
      message: `Your food request for ${newReq.category} (${newReq.quantity}) has been submitted.`,
      is_read: false,
      created_at: new Date().toISOString()
    };
    const updatedNotifs = [newNotif, ...notifications];
    setNotifications(updatedNotifs);
    persist("mealbridge_notifs", updatedNotifs);

    return newReq;
  };

  const updateDonationStatus = (id: string, status: string) => {
    const updated = donations.map((d) => (d.id === id ? { ...d, status } : d));
    setDonations(updated);
    persist("mealbridge_donations", updated);
  };

  const updateRequestStatus = (id: string, status: string) => {
    const updated = requests.map((r) => (r.id === id ? { ...r, status } : r));
    setRequests(updated);
    persist("mealbridge_requests", updated);
  };

  const deleteDonation = (id: string) => {
    const updated = donations.filter((d) => d.id !== id);
    setDonations(updated);
    persist("mealbridge_donations", updated);
  };

  const toggleUserStatus = (id: string) => {
    const updated = users.map((u) =>
      u.id === id ? { ...u, status: u.status === "Active" ? "Inactive" : "Active" } : u
    );
    setUsers(updated);
    persist("mealbridge_users", updated);
  };

  const markNotificationRead = (id: string) => {
    const updated = notifications.map((n) => (n.id === id ? { ...n, is_read: true } : n));
    setNotifications(updated);
    persist("mealbridge_notifs", updated);
  };

  const markAllNotificationsRead = () => {
    const updated = notifications.map((n) => ({ ...n, is_read: true }));
    setNotifications(updated);
    persist("mealbridge_notifs", updated);
  };

  const stats = {
    totalUsers: users.length,
    totalDonations: donations.length,
    totalRequests: requests.length,
    activeDonations: donations.filter((d) => d.status === "Active" || d.status === "Approved").length,
    completedDonations: donations.filter((d) => d.status === "Completed").length,
    mealsShared: donations.reduce((sum, d) => sum + (Number(d.people_served) || 0), 0)
  };

  return (
    <StoreContext.Provider
      value={{
        currentUser,
        donations,
        requests,
        notifications,
        users,
        login,
        register,
        logout,
        updateProfile,
        addDonation,
        addRequest,
        updateDonationStatus,
        updateRequestStatus,
        deleteDonation,
        toggleUserStatus,
        markNotificationRead,
        markAllNotificationsRead,
        stats
      }}
    >
      {children}
    </StoreContext.Provider>
  );
}

export function useStore() {
  const context = useContext(StoreContext);
  if (!context) {
    throw new Error("useStore must be used within a StoreProvider");
  }
  return context;
}
