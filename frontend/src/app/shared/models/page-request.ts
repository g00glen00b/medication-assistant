export class PageRequest {
    public readonly page: number = 0;
    public readonly size: number = 10;
    public readonly sort?: string;


    constructor(initialValues?: Partial<PageRequest>) {
        Object.assign(this, initialValues);
    }

    next(): PageRequest {
        return new PageRequest({page: this.page + 1, size: this.size, sort: this.sort});
    }

    calculateIndexRange(): [number, number] {
        const startIndex: number = this.page * this.size;
        const endIndex: number = startIndex + this.size;
        return [startIndex, endIndex];
    }

    static firstUnsortedPage(size: number): PageRequest {
        return new PageRequest({page: 0, size, sort: ''});
    }

    static firstSortedPage(size: number, sort: string): PageRequest {
        return new PageRequest({page: 0, size, sort});
    }
}

export const FIRST_TEN_RESULTS: PageRequest = PageRequest.firstUnsortedPage(10);