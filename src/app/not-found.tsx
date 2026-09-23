import Link from "next/link";
import { Heart, ArrowLeft } from "lucide-react";

export default function NotFound() {
  return (
    <div className="min-h-[70vh] flex items-center justify-center px-4 py-16">
      <div className="max-w-md w-full text-center space-y-6 bg-white p-8 rounded-2xl border border-slate-200 shadow-sm">
        <div className="w-14 h-14 rounded-2xl bg-orange-100 text-orange-700 flex items-center justify-center mx-auto">
          <Heart className="w-8 h-8 fill-orange-600 text-orange-600" />
        </div>
        <div className="space-y-2">
          <h1 className="text-3xl font-extrabold text-slate-900">404 - Page Not Found</h1>
          <p className="text-xs text-slate-500">
            The requested community page could not be located.
          </p>
        </div>
        <Link
          href="/"
          className="inline-flex items-center gap-2 px-5 py-2.5 bg-green-600 hover:bg-green-700 text-white rounded-xl text-xs font-semibold shadow-sm transition-colors"
        >
          <ArrowLeft className="w-4 h-4" /> Back to MealBridge Home
        </Link>
      </div>
    </div>
  );
}
