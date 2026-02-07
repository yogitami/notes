# MacOS Terminal Keyboard Shortcuts Cheat Sheet

## Cursor Movement
| Action | Shortcut |
|------|---------|
| Move cursor left | `←` |
| Move cursor right | `→` |
| Beginning of line | `Ctrl + A` |
| End of line | `Ctrl + E` |
| Move back one word | `Option (Alt) + ←` |
| Move forward one word | `Option (Alt) + →` |

---

## Text Deletion
| Action | Shortcut |
|------|---------|
| Delete character before cursor | `Delete` |
| Delete character after cursor (forward delete) | `Fn + Delete` |
| Delete word before cursor | `Ctrl + W` |
| Delete word after cursor | `Option (Alt) + D` |
| Delete to end of line | `Ctrl + K` |
| Delete to beginning of line | `Ctrl + U` |
| Clear entire line | `Ctrl + U` |

---

## Line Editing & Control
| Action | Shortcut |
|------|---------|
| Clear screen | `Ctrl + L` |
| Cancel current command | `Ctrl + C` |
| Suspend current process | `Ctrl + Z` |
| Resume suspended process | `fg` |
| Run last command again | `!!` |

---

## Command History
| Action | Shortcut |
|------|---------|
| Previous command | `↑` |
| Next command | `↓` |
| Search command history | `Ctrl + R` |
| Exit history search | `Ctrl + G` |
| Repeat last command | `!!` |
| Repeat command by number | `!<number>` |

---

## Cut, Copy, Paste (Terminal)
| Action | Shortcut |
|------|---------|
| Copy | `Cmd + C` |
| Paste | `Cmd + V` |
| Paste without formatting | `Cmd + Shift + V` |

---

## Tab Completion
| Action | Shortcut |
|------|---------|
| Auto-complete command or path | `Tab` |
| Show all completion options | `Tab + Tab` |

---

## Terminal Window & Tabs
| Action | Shortcut |
|------|---------|
| New tab | `Cmd + T` |
| Close tab | `Cmd + W` |
| New window | `Cmd + N` |
| Split pane vertically | `Cmd + D` |
| Split pane horizontally | `Cmd + Shift + D` |
| Switch tabs | `Cmd + Shift + [` / `]` |

---

## Process & Job Control
| Action | Shortcut |
|------|---------|
| Interrupt process | `Ctrl + C` |
| Suspend process | `Ctrl + Z` |
| List background jobs | `jobs` |
| Resume job in foreground | `fg` |
| Resume job in background | `bg` |

---

## Useful Tips
- Enable **Option as Meta key**:  
  `Terminal → Settings → Profiles → Keyboard → Use Option as Meta key`
- Works best with **zsh** (default macOS shell)

---

# Vim Keyboard Shortcuts Cheat Sheet

## Modes
| Mode | Key |
|----|----|
| Normal mode | `Esc` |
| Insert mode | `i` |
| Insert at line start | `I` |
| Insert at line end | `A` |
| Visual mode | `v` |
| Visual line mode | `V` |
| Visual block mode | `Ctrl + v` |
| Command mode | `:` |

---

## Cursor Movement
| Action | Key |
|----|----|
| Left / Down / Up / Right | `h` `j` `k` `l` |
| Beginning of line | `0` |
| First non-blank | `^` |
| End of line | `$` |
| Next word | `w` |
| Previous word | `b` |
| End of word | `e` |
| Top of file | `gg` |
| Bottom of file | `G` |
| Jump to line | `:<line>` |

---

## Insert Mode Editing
| Action | Key |
|----|----|
| Insert before cursor | `i` |
| Insert after cursor | `a` |
| New line below | `o` |
| New line above | `O` |
| Exit insert mode | `Esc` |

---

## Deleting Text
| Action | Key |
|----|----|
| Delete character | `x` |
| Delete previous character | `X` |
| Delete word | `dw` |
| Delete to end of word | `de` |
| Delete line | `dd` |
| Delete to end of line | `D` |
| Delete N lines | `d<N>d` |

---

## Copy (Yank) & Paste
| Action | Key |
|----|----|
| Yank line | `yy` |
| Yank word | `yw` |
| Yank selection | `y` |
| Paste after cursor | `p` |
| Paste before cursor | `P` |

---

## Undo & Redo
| Action | Key |
|----|----|
| Undo | `u` |
| Redo | `Ctrl + r` |

---

## Search & Replace
| Action | Key |
|----|----|
| Search forward | `/text` |
| Search backward | `?text` |
| Next match | `n` |
| Previous match | `N` |
| Replace in line | `:s/old/new/` |
| Replace in file | `:%s/old/new/g` |
| Replace with confirmation | `:%s/old/new/gc` |

---

## Visual Mode
| Action | Key |
|----|----|
| Select | `v` |
| Select lines | `V` |
| Select block | `Ctrl + v` |
| Indent right | `>` |
| Indent left | `<` |

---

## File Operations
| Action | Command |
|----|----|
| Save | `:w` |
| Save as | `:w filename` |
| Quit | `:q` |
| Save & quit | `:wq` |
| Quit without saving | `:q!` |
| Open file | `:e filename` |

---

## Window & Split Management
| Action | Command |
|----|----|
| Vertical split | `:vsplit` |
| Horizontal split | `:split` |
| Switch window | `Ctrl + w w` |
| Close split | `:q` |
| Equalize splits | `Ctrl + w =` |

---

## Buffers
| Action | Command |
|----|----|
| List buffers | `:ls` |
| Switch buffer | `:b <number>` |
| Next buffer | `:bn` |
| Previous buffer | `:bp` |
| Delete buffer | `:bd` |

---

## Productivity Combos
| Action | Keys |
|----|----|
| Change word | `cw` |
| Change line | `cc` |
| Change to end of line | `C` |
| Repeat last command | `.` |
| Join lines | `J` |

---

## Exit Panic Mode 😄
| Situation | Command |
|----|----|
| Vim won’t let me type | `Esc` |
| Stuck somewhere | `Esc Esc` |
| Force quit | `:q!` |

