import { NextResponse } from 'next/server';

const JAVA_BACKEND_URL = process.env.JAVA_BACKEND_URL || 'http://localhost:8080';

export async function GET(request: Request) {
    const { searchParams } = new URL(request.url);
    const userId = searchParams.get('userId');

    if (!userId) {
        return NextResponse.json({ error: 'User ID is required' }, { status: 400 });
    }

    try {
        const res = await fetch(`${JAVA_BACKEND_URL}/cart/carts`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ filters: { userId } }),
            cache: 'no-store',
        });
        const data = await res.json();
        return NextResponse.json(data);
    } catch (error) {
        return NextResponse.json({ error: 'Failed to fetch cart' }, { status: 500 });
    }
}

export async function POST(request: Request) {
    try {
        const body = await request.json();
        const res = await fetch(`${JAVA_BACKEND_URL}/cart/items`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body),
        });
        const data = await res.json();
        return NextResponse.json(data);
    } catch (error) {
        return NextResponse.json({ error: 'Failed to update cart' }, { status: 500 });
    }
}
