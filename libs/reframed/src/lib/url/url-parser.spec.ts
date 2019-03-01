import { UrlParser } from './url-parser';

describe('UrlParser', () => {

    it('parses sparkles://appName', () => {
        const url = 'sparkles://appName';
        const result = new UrlParser(url, 'sparkles://').parse();
        expect(result.url).toBe(url);
        expect(result.appName).toBe('appName');
    });

    it('parses sparkles://appName/entryPoint', () => {
        const url = 'sparkles://appName/entryPoint';
        const result = new UrlParser(url, 'sparkles://').parse();
        expect(result.url).toBe(url);
        expect(result.appName).toBe('appName');
        expect(result.entryPoint).toBe('entryPoint');
    });

    it('parses sparkles://app/entry?a=b', () => {
        const url = 'sparkles://app/entry?a=b';
        const result = new UrlParser(url, 'sparkles://').parse();
        expect(result.params).toBeTruthy();
        expect(result.params.a).toBe('b');
    });

    it('parses sparkles://app/entry?a=b&x=yz', () => {
        const url = 'sparkles://app/entry?a=b&x=yz';
        const result = new UrlParser(url, 'sparkles://').parse();
        expect(result.params).toBeTruthy();
        expect(result.params.a).toBe('b');
        expect(result.params.x).toBe('yz');
    });

    it('parses sparkles://app/entry?a=b&a=x&a=y', () => {
        const url = 'sparkles://app/entry?a=b&a=x&a=y';
        const result = new UrlParser(url, 'sparkles://').parse();
        expect(result.params).toBeTruthy();
        expect(result.params.a.length).toEqual(3);
        expect(result.params.a[0]).toEqual('b');
        expect(result.params.a[1]).toEqual('x');
        expect(result.params.a[2]).toEqual('y');
    });

    it('parses sparkles://app/entry?a', () => {
        const url = 'sparkles://app/entry?a';
        const result = new UrlParser(url, 'sparkles://').parse();
        expect(result.params).toBeTruthy();
        expect(result.params.a).toEqual('');
    });

    it('parses sparkles://app/entry?a&a', () => {
        const url = 'sparkles://app/entry?a&a';
        const result = new UrlParser(url, 'sparkles://').parse();
        expect(result.params.a.length).toEqual(2);
        expect(result.params.a[0]).toEqual('');
        expect(result.params.a[1]).toEqual('');
    });

});
