'use client';

import { useState, useEffect } from 'react';

export default function CartPage() {
    const [cart, setCart] = useState<any>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetch('/api/cart?userId=user-123')
            .then((res) => res.json())
            .then((data) => {
                setCart(data.payload?.data?.[0] || null);
                setLoading(false);
            });
    }, []);

    if (loading) return <div className="p-8 text-center">Loading cart...</div>;

    return (
        <div className="max-w-4xl mx-auto p-8">
            <h1 className="text-3xl font-bold mb-8">Shopping Cart</h1>
            {!cart || !cart.items || cart.items.length === 0 ? (
                <div className="text-center py-12 bg-gray-50 rounded-lg">
                    <p className="text-gray-500">Your cart is empty</p>
                    <a href="/" className="text-indigo-600 mt-4 inline-block font-medium">Continue Shopping</a>
                </div>
            ) : (
                <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
                    <div className="md:col-span-2 space-y-4">
                        {cart.items.map((item: any) => (
                            <div key={item.productId} className="flex gap-4 p-4 bg-white border rounded-lg shadow-sm">
                                <div className="flex-1">
                                    <h3 className="font-semibold text-lg">{item.productName || 'Product Name'}</h3>
                                    <p className="text-gray-500 text-sm">Qty: {item.quantity}</p>
                                </div>
                                <div className="text-right">
                                    <p className="font-bold text-lg">${item.price?.amount || 0}</p>
                                </div>
                            </div>
                        ))}
                    </div>
                    <div className="bg-gray-50 p-6 rounded-lg h-fit">
                        <h2 className="text-xl font-bold mb-4">Summary</h2>
                        <div className="space-y-2 border-b pb-4 mb-4">
                            <div className="flex justify-between">
                                <span className="text-gray-600">Subtotal</span>
                                <span>${cart.totalAmount?.amount || 0}</span>
                            </div>
                        </div>
                        <div className="flex justify-between font-bold text-lg mb-6">
                            <span>Total</span>
                            <span>${cart.totalAmount?.amount || 0}</span>
                        </div>
                        <button className="w-full py-3 bg-indigo-600 text-white rounded-lg font-bold hover:bg-indigo-700 transition">
                            Checkout
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
}
