import { unique } from './functional';

describe(`unique()`, () => {

  it(`should reduce duplicate values`, () => {
    const foo = [ '1', '2', '1', '4' ];
    expect(unique(foo)).toEqual(['1', '2', '4']);
  });

  it(`should determine duplicates by calling comparator function`, () => {
    const foo = [ '1', '2', '1', '4' ];
    expect(unique(foo, (a, b) => false)).toEqual(['1', '2', '1', '4']);

    const bar = [
      { id: 0, value: 'zero' },
      { id: 1, value: 'one' },
      { id: 0, value: 'two' },
    ];
    const result = unique(bar, (a, b) => a.id === b.id);
    expect(result).toHaveLength(2);
    expect(result[0].value).toEqual('zero');
  });

});
