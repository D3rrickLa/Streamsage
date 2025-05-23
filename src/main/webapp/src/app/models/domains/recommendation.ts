import { AvailableService } from "./available-service";
import { Genre } from "./genre";

export class Recommendation {
    title?: string;
    description?: string;
    posterURL?: string;
    genres?: Genre[];
    releaseDate?: string;
    availableService?: AvailableService[];
}
