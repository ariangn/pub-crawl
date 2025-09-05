import { SignupForm } from "@/features/auth/components/signup-form"

export default function SignupPage() {
  return (
    <div className="flex min-h-svh w-full items-center justify-center p-6 md:p-10">
      <div className="absolute top-4 left-4">
        <h1 className="text-2xl font-bold">pubcrawl</h1>
      </div>
      <div className="w-full max-w-sm">
        <SignupForm />
      </div>
    </div>
  )
}