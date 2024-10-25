import create from "zustand";

export const useAppStore = create((set) => ({
  dopen: false,
  updateOpen: (open) => set({ dopen: open }),
}));
