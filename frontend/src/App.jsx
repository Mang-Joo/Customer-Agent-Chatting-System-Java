import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Home from "./pages/user/Home";
import Dashboard from "./pages/agent/Dashboard";

import './App.css'

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Navigate to="/login" />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="user/home" element={<Home />} />
        <Route path="agent/dashboard" element={<Dashboard />} />
      </Routes>
    </Router>
  )
}

export default App;
