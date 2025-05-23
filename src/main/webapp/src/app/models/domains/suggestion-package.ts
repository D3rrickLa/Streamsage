import { Recommendation } from "./recommendation";

export class SuggestionPackage {
    userPrompt?: string;
    timestamp?: number;
    recommendationList?: Recommendation[];

}
