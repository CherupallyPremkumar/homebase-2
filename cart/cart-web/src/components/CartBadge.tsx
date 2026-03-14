'use client';

import { useState, useEffect } from 'react';

export default function CartBadge() {
    const [count, setCount] = useState(0);

    useEffect(() => {
        fetch('/api/cart?userId=user-123')
            .then((res) => res.json())
            .then((data) => {
                const cart = data.payload?.data?.[0];
                setCount(cart?.items?.length || 0);
            });
    }, []);

    return (
        <div className="relative inline-block">
            <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6 text-gray-700" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
            </svg>
            {count > 0 && (
                <span className="absolute -top-2 -right-2 bg-red-600 text-white text-[10px] font-bold px-1.5 py-0.5 rounded-full min-w-[18px] text-center">
                    {count}
                </span>
            )}
        </div>
    );
}
