export interface StartAdventureRequest {
  bookId: number;
  playerName: string;
}

export interface MakeChoiceRequest {
  optionIndex: number;
}

export interface AdventureState {
  id: number;
  bookId: number;
  playerName: string;
  health: number;
  status: string;
  currentSection: CurrentSection | null;
  consequenceMessage: string | null;
}

export interface CurrentSection {
  id: number;
  text: string;
  type: string;
  choices: AdventureChoice[];
}

export interface AdventureChoice {
  index: number;
  description: string;
}
