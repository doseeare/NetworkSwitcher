# 📡 NetworkSwitcher

**NetworkSwitcher** is a desktop application for **macOS**, built with **Kotlin + Compose Desktop**, that allows you to manage the priority of network devices.  
It leverages the native `networksetup` utility to list available network services and reorder them.

---

## ✨ Features
- 🔍 Retrieve all available network devices on macOS.
- ⚡ Instantly change the priority of the selected device.
- 🎨 Modern UI powered by **Compose Desktop**:
  - Device list with highlighted selection.
  - Notifications for success or failure.
  - Minimalistic dark theme design.

---

## 🛠️ Tech Stack
- **Kotlin**
- **Compose Desktop**
- **ProcessBuilder** for executing system commands
- macOS utility: `networksetup`

---

## 📂 Project Structure
- `Terminal` — handles system commands (`listnetworkserviceorder`, `ordernetworkservices`).
- `App` — Compose Desktop UI.
- `main` — application entry point.

---

## 🚀 Installation & Run
1. Make sure you have **JDK 17+** and **Gradle** installed.
