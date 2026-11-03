export interface BookSummary {
  id: number;
  title: string;
  author: string;
  valid: boolean;
}

export interface BookResponse {
  id: number;
  title: string;
  author: string;
  sections: BookSection[];
}

export interface BookSection {
  id: number;
  text: string;
  type: string;
  options: BookOption[];
}

export interface BookOption {
  description: string;
  goToId: number | null;
  consequence: BookConsequence | null;
}

export interface BookConsequence {
  type: string;
  value: number | null;
  text: string | null;
}

export interface BookValidationResponse {
  bookId: number;
  valid: boolean;
  errors: BookValidationError[];
}

export interface BookValidationError {
  code: string;
  message: string;
  sectionId: number | null;
}

export interface UnreachableSectionsResponse {
  bookId: number;
  unreachableSectionIds: number[];
  hasUnreachableSections?: boolean;
}
