import { SuggestionPackage } from "./domains/suggestion-package";

export class Feedback {
    id?: number;
    rating?: number;
    comment?: string;
    suggestionPackage?: SuggestionPackage;
}
