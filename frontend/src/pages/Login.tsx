import React, { useState } from "react";
import { Button, Field, Fieldset, Input, Label, Legend } from "@headlessui/react";
import clsx from "clsx";
import { login } from "../api";
import { useNavigate } from "react-router-dom";

export default function Login() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    email: "", 
    password: ""
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleClick = async () => {
    const response = await login(formData);
    if (response) {
      if (response.role == "AGENT") navigate("/agent/dashboard");
      else navigate("/user/home");
    } else {
      alert("Sign in failed. Please try again.");
    }
  };

  return (
      <Fieldset className="w-full max-w-md space-y-6 rounded-xl bg-gray-900 p-8 shadow-lg">
        {/* Title */}
        <Legend className="text-center text-2xl font-semibold text-white">SIGN IN</Legend>

        {/* Email Input Field */}
        <Field className="">
          <Label className="flex text-left text-sm font-medium text-white ml-2">Email</Label>
          <Input
            name="email"
            value={formData.email}
            onChange={handleChange}
            type="email"
            className={clsx(
              "mt-2 w-full rounded-lg border-none bg-white/10 py-2 px-4 text-sm text-white",
              "focus:outline-none focus:ring-2 focus:ring-white/25"
            )}
          />
        </Field>

        {/* Password Input Field */}
        <Field className="">
          <Label className="flex text-left text-sm font-medium text-white ml-2">Password</Label>
          <Input
            name="password"
            value={formData.password}
            onChange={handleChange}
            type="password"
            className={clsx(
              "mt-2 w-full rounded-lg border-none bg-white/10 py-2 px-4 text-sm text-white",
              "focus:outline-none focus:ring-2 focus:ring-white/25"
            )}
          />
        </Field>

        {/* Sign in Button */}
        <Field className="justify-center">
          <Button
            onClick={handleClick}
            className="w-[300px] rounded-md bg-gray-700 py-2 text-sm font-semibold text-white 
                      shadow-lg transition-all hover:bg-gray-600 focus:outline-none 
                      focus:ring-2 focus:ring-white/25"
            >
            Sign In
          </Button>
        </Field>
      </Fieldset>
  );
}
