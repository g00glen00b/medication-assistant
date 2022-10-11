import {Duration} from "date-fns";
import {parse} from "tinyduration";

export function parseAndNormalizeDuration(value: string): Duration {
  const durationObject: Duration = parse(value);
  const {days} = durationObject;
  const weeks = days == null ? 0 : Math.floor(days / 7);
  const normalizedDays = days == null ? 0 : days % 7;
  return {...durationObject, weeks, days: normalizedDays};
}