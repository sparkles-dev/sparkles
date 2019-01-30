import { UrlParser } from './url-parser';

describe('UrlParser', () => {

    it('parses u://appName', () => {
        const url = 'u://appName';
        const result = new UrlParser(url).parse();
        expect(result.url).toBe(url);
        expect(result.appName).toBe('appName');
    });

    it('parses u://appName/entryPoint', () => {
        const url = 'u://appName/entryPoint';
        const result = new UrlParser(url).parse();
        expect(result.url).toBe(url);
        expect(result.appName).toBe('appName');
        expect(result.entryPoint).toBe('entryPoint');
    });

    it('parses u://app/entry?a=b', () => {
        const url = 'u://app/entry?a=b';
        const result = new UrlParser(url).parse();
        expect(result.params).toBeTruthy();
        expect(result.params.a).toBe('b');
    });

    it('parses u://app/entry?a=b&x=yz', () => {
        const url = 'u://app/entry?a=b&x=yz';
        const result = new UrlParser(url).parse();
        expect(result.params).toBeTruthy();
        expect(result.params.a).toBe('b');
        expect(result.params.x).toBe('yz');
    });

    it('parses u://app/entry?a=b&a=x&a=y', () => {
        const url = 'u://app/entry?a=b&a=x&a=y';
        const result = new UrlParser(url).parse();
        expect(result.params).toBeTruthy();
        expect(result.params.a.length).toEqual(3);
        expect(result.params.a[0]).toEqual('b');
        expect(result.params.a[1]).toEqual('x');
        expect(result.params.a[2]).toEqual('y');
    });

    it('parses u://app/entry?a', () => {
        const url = 'u://app/entry?a';
        const result = new UrlParser(url).parse();
        expect(result.params).toBeTruthy();
        expect(result.params.a).toEqual('');
    });

    it('parses u://app/entry?a&a', () => {
        const url = 'u://app/entry?a&a';
        const result = new UrlParser(url).parse();
        expect(result.params.a.length).toEqual(2);
        expect(result.params.a[0]).toEqual('');
        expect(result.params.a[1]).toEqual('');
    });

});
