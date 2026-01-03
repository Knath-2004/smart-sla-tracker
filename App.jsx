import { useEffect, useRef, useState } from "react";
import {
  BrowserRouter,
  Routes,
  Route,
  Navigate,
  Link,
  useNavigate,
} from "react-router-dom";
import axios from "axios";
import "./App.css";

/* ================= API ================= */

const api = axios.create({
  baseURL: "http://localhost:8081",
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

api.interceptors.response.use(
  (res) => res,
  (err) => {
    if (err.response?.status === 401) {
      localStorage.clear();
      window.location.href = "/login";
    }
    return Promise.reject(err);
  }
);

/* ================= JWT ROLE ================= */

const getRole = () => {
  const token = localStorage.getItem("token");
  if (!token) return null;
  try {
    const payload = JSON.parse(atob(token.split(".")[1]));
    if (payload.role) return payload.role.replace("ROLE_", "");
    if (payload.authorities?.length)
      return payload.authorities[0].replace("ROLE_", "");
    return null;
  } catch {
    return null;
  }
};

/* ================= SLA HELPERS ================= */

const getRemainingMs = (deadline) =>
  deadline ? new Date(deadline) - new Date() : null;

const getRemainingTime = (deadline) => {
  const diff = getRemainingMs(deadline);
  if (diff === null) return "No SLA";
  if (diff <= 0) return "BREACHED";
  const h = Math.floor(diff / 3600000);
  const m = Math.floor((diff / 60000) % 60);
  return `${h}h ${m}m left`;
};

const isBreachingSoon = (deadline) =>
  deadline && getRemainingMs(deadline) < 1000 * 60 * 60 * 2;

const getSlaPercent = (createdAt, deadline) => {
  if (!createdAt || !deadline) return 0;
  const total = new Date(deadline) - new Date(createdAt);
  const used = new Date() - new Date(createdAt);
  return Math.min(100, Math.max(0, (used / total) * 100));
};

/* ================= UI HELPERS ================= */

const Spinner = () => <div className="spinner"></div>;

const Banner = ({ type, message }) =>
  message ? <div className={`banner ${type}`}>{message}</div> : null;

const StatusBadge = ({ status }) => (
  <span className={`status ${status.toLowerCase()}`}>{status}</span>
);

/* ================= APPLE DROPDOWN ================= */

function AppleDropdown({ options, value, onChange }) {
  const [open, setOpen] = useState(false);
  const ref = useRef(null);

  useEffect(() => {
    const close = (e) =>
      ref.current && !ref.current.contains(e.target) && setOpen(false);
    document.addEventListener("mousedown", close);
    return () => document.removeEventListener("mousedown", close);
  }, []);

  return (
    <div className="apple-dropdown" ref={ref}>
      <button
        type="button"
        className={`apple-trigger ${open ? "open" : ""}`}
        onClick={() => setOpen(!open)}
      >
        {value} <span className="arrow">⌄</span>
      </button>

      {open && (
        <div className="apple-menu">
          {options.map((opt) => (
            <div
              key={opt}
              className={`apple-option ${opt === value ? "selected" : ""}`}
              onClick={() => {
                onChange(opt);
                setOpen(false);
              }}
            >
              {opt}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

/* ================= DARK MODE ================= */

function DarkModeToggle() {
  useEffect(() => {
    if (localStorage.getItem("dark") === "true") {
      document.body.classList.add("dark");
    }
  }, []);

  return (
    <button
      className="secondary"
      onClick={() => {
        document.body.classList.toggle("dark");
        localStorage.setItem(
          "dark",
          document.body.classList.contains("dark")
        );
      }}
    >
      🌙 Dark Mode
    </button>
  );
}

function LogoutButton() {
  const navigate = useNavigate();
  return (
    <button
      className="logout"
      onClick={() => {
        localStorage.clear();
        navigate("/login");
      }}
    >
      Logout
    </button>
  );
}

/* ================= AUTH ================= */

function Login() {
  const navigate = useNavigate();
  const [error, setError] = useState("");

  const login = async (e) => {
    e.preventDefault();
    try {
      const res = await api.post(
        "/auth/login",
        Object.fromEntries(new FormData(e.target))
      );
      localStorage.setItem("token", res.data);
      navigate("/");
    } catch {
      setError("Invalid username or password");
    }
  };

  return (
    <div className="card">
      <h2>Login</h2>
      <Banner type="error" message={error} />
      <form onSubmit={login}>
        <input name="username" placeholder="Username" required />
        <input name="password" type="password" placeholder="Password" required />
        <button>Login</button>
      </form>
      <p className="nav-text">
        New user? <Link to="/register">Register</Link>
      </p>
    </div>
  );
}

function Register() {
  const navigate = useNavigate();
  const [success, setSuccess] = useState("");

  const register = async (e) => {
    e.preventDefault();
    const form = Object.fromEntries(new FormData(e.target));
    await api.post("/auth/register", form);
    setSuccess("Registered successfully. Redirecting...");
    setTimeout(() => navigate("/login"), 1000);
  };

  return (
    <div className="card">
      <h2>Register</h2>
      <Banner type="success" message={success} />
      <form onSubmit={register}>
        <input name="username" placeholder="Username" required />
        <input name="email" placeholder="Email" required />
        <input name="password" type="password" placeholder="Password" required />
        <button>Register</button>
      </form>
      <p className="nav-text">
        Already have an account? <Link to="/login">Login</Link>
      </p>
    </div>
  );
}


/* ================= LAYOUT ================= */

const Layout = ({ title, children }) => (
  <div className="layout">
    <aside className="sidebar">
      <h3>Smart SLA</h3>
      <p>{title}</p>
      <DarkModeToggle />
      <LogoutButton />
    </aside>
    <main className="content">{children}</main>
  </div>
);

/* ================= USER DASHBOARD ================= */

function UserDashboard() {
  const [tickets, setTickets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [category, setCategory] = useState("PAYMENT_ISSUE");
  const [selectedTicket, setSelectedTicket] = useState(null);

  const loadTickets = async () => {
    setLoading(true);
    const res = await api.get("/tickets");
    setTickets(res.data);
    setLoading(false);
  };

  useEffect(() => {
    loadTickets();
    const timer = setInterval(loadTickets, 60000);
    return () => clearInterval(timer);
  }, []);

  const createTicket = async (e) => {
    e.preventDefault();
    const data = Object.fromEntries(new FormData(e.target));
    data.category = category;
    await api.post("/tickets", data);
    e.target.reset();
    loadTickets();
  };

  return (
    <Layout title="User Dashboard">
      {/* 🔹 CENTERING CONTAINER */}
      <div className="dashboard-container">

        {/* ================= CREATE TICKET ================= */}
        <div className="panel panel-wide">
          <h3>Create Ticket</h3>

          <form className="form-grid" onSubmit={createTicket}>
            <input name="title" placeholder="Title" required />
            <input name="description" placeholder="Description" required />

            <AppleDropdown
              options={[
                "PAYMENT_ISSUE",
                "NETWORK_ISSUE",
                "LOGIN_ISSUE",
                "GENERAL_QUERY",
              ]}
              value={category}
              onChange={setCategory}
            />

            <button>Create</button>
          </form>
        </div>

        {/* ================= MY TICKETS ================= */}
        <div className="panel panel-wide">
          <h3>My Tickets</h3>

          {loading ? (
            <Spinner />
          ) : tickets.length === 0 ? (
            <div className="empty-state">
              🎫 You haven’t created any tickets yet
            </div>
          ) : (
            tickets.map((t) => {
              const percent = getSlaPercent(t.createdAt, t.slaDeadline);
              const breachSoon = isBreachingSoon(t.slaDeadline);

              return (
                <div
                  key={t.id}
                  className={`ticket-row ticket-animate ${
                    breachSoon ? "breach-warning" : ""
                  }`}
                  onClick={() => setSelectedTicket(t)}
                >
                  <div className="ticket-left">
                    <div className="ticket-title">{t.title}</div>

                    {/* SLA PROGRESS BAR */}
                    <div className="sla-bar">
                      <div
                        className={`sla-fill ${
                          percent > 85
                            ? "danger"
                            : percent > 60
                            ? "warn"
                            : ""
                        }`}
                        style={{ width: `${percent}%` }}
                      />
                    </div>

                    <div className="ticket-meta">
                      {getRemainingTime(t.slaDeadline)}
                    </div>
                  </div>

                  <StatusBadge status={t.status} />
                </div>
              );
            })
          )}
        </div>

        {/* ================= MODAL ================= */}
        {selectedTicket && (
          <div
            className="modal-backdrop"
            onClick={() => setSelectedTicket(null)}
          >
            <div className="modal" onClick={(e) => e.stopPropagation()}>
              <h3>🎫 Ticket Details</h3>

              <div className="modal-row">
                <b>Title</b>
                <span>{selectedTicket.title}</span>
              </div>

              <div className="modal-row">
                <b>Description</b>
                <span>{selectedTicket.description}</span>
              </div>

              <div className="modal-row">
                <b>Status</b>
                <StatusBadge status={selectedTicket.status} />
              </div>

              <div className="modal-row">
                <b>SLA</b>
                <span>{getRemainingTime(selectedTicket.slaDeadline)}</span>
              </div>

              <button
                className="secondary"
                onClick={() => setSelectedTicket(null)}
              >
                Close
              </button>
            </div>
          </div>
        )}
      </div>
    </Layout>
  );
}

/* ================= ADMIN DASHBOARD ================= */


function AdminDashboard() {
  const [tickets, setTickets] = useState([]);
  const [loading, setLoading] = useState(true);

  const loadTickets = async () => {
    setLoading(true);
    const res = await api.get("/admin/tickets");
    setTickets(res.data);
    setLoading(false);
  };

  useEffect(() => {
    loadTickets();
  }, []);

  const breachingSoon = tickets.filter(t =>
    isBreachingSoon(t.slaDeadline)
  ).length;

  return (
    <Layout title="Admin Dashboard">
      <div className="admin-container">

        {/* ===== SLA ANALYTICS ===== */}
        <div className="admin-analytics">
          <div className="analytics-card">
            <h3>SLA Analytics</h3>
            <p>Total Tickets: <b>{tickets.length}</b></p>
            <p>Breaching Soon: <b>{breachingSoon}</b></p>
          </div>
        </div>

        {/* ===== ALL TICKETS ===== */}
        <div className="admin-tickets">
          <h3>All Tickets</h3>

          {loading ? (
            <Spinner />
          ) : (
            <div className="ticket-grid">
              {tickets.map(t => (
                <div key={t.id} className="admin-ticket-card">
                  <div className="ticket-info">
                    <h4>{t.title}</h4>
                    <p className="ticket-meta">
                      {getRemainingTime(t.slaDeadline)}
                    </p>
                  </div>

                  <StatusBadge status={t.status} />

                  <div className="admin-actions">
                    <button
                      onClick={() =>
                        api.put(`/admin/tickets/${t.id}/resolve`)
                          .then(loadTickets)
                      }
                    >
                      Resolve
                    </button>

                    <button
                      className="danger"
                      onClick={() =>
                        api.put(`/admin/tickets/${t.id}/escalate`)
                          .then(loadTickets)
                      }
                    >
                      Escalate
                    </button>

                    <button className="secondary">
                      View Audit
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

      </div>
    </Layout>
  );
}


/* ================= ROUTING ================= */

function Dashboard() {
  const role = getRole();
  if (role === "USER") return <UserDashboard />;
  if (role === "ADMIN") return <AdminDashboard />;
  return <Navigate to="/login" />;
}

/* ================= APP ================= */

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/" element={<Dashboard />} />
      </Routes>
    </BrowserRouter>
  );
}


