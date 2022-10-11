export interface PageInfo<T> {
    content: T[];
    first: boolean;
    last: boolean;
    totalElements: number;
    totalPages: number;
    empty: boolean;
}

export const EMPTY_PAGE_INFO: PageInfo<any> = {
  content: [],
  first: true,
  last: true,
  totalElements: 0,
  totalPages: 1,
  empty: true
};