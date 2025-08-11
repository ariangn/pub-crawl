// minimal api client with jwt support and base url switching
const BASE_URL = import.meta.env.VITE_API_BASE_URL as string;

let accessToken: string | null = null;
export function setAccessToken(token: string | null) {
  accessToken = token;
}

type FetchOptions = RequestInit & { auth?: boolean };

export async function api(path: string, opts: FetchOptions = {}) {
  const headers: Record<string, string> = {
    'content-type': 'application/json',
    ...(opts.headers as Record<string, string> ?? {})
  };

  if (opts.auth && accessToken) {
    headers['authorization'] = `Bearer ${accessToken}`;
  }

  const res = await fetch(`${BASE_URL}${path}`, { ...opts, headers });

  if (!res.ok) {
    const maybeJson = await res.clone().text().catch(() => '');
    let message = maybeJson;
    try { message = JSON.parse(maybeJson).message ?? maybeJson; } catch {
      // intentionally ignore JSON parse errors
    }
    throw new Error(`${res.status} ${res.statusText}: ${message || 'request failed'}`);
  }

  // handle 204
  if (res.status === 204) return null;

  const ct = res.headers.get('content-type') || '';
  return ct.includes('application/json') ? res.json() : res.text();
}

// helpers
export const get = (p: string, auth = true) => api(p, { method: 'GET', auth });
export const post = (p: string, body?: unknown, auth = true) =>
  api(p, { method: 'POST', body: body ? JSON.stringify(body) : undefined, auth });
export const del = (p: string, auth = true) => api(p, { method: 'DELETE', auth });
