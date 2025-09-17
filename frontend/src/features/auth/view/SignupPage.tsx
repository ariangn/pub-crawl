import { SignupForm } from "@/features/auth/components/signup-form"
import { NavBar } from "@/features/common/components/nav-bar"

export default function SignupPage() {
  return (
    <div className="min-h-svh">
      <NavBar />
      <div className="flex w-full items-center justify-center p-6 md:p-10">
        <div className="w-full max-w-sm">
          <SignupForm />
        </div>
      </div>
    </div>
  )
}