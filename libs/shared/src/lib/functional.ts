export function unique<T> (value: T[], comparator?: (a: T, b: T) => boolean) {
  if (!comparator) {
    comparator = (a, b) => a === b;
  }

  return value.reduce((prev, current) => {
    if (prev.find(item => comparator(item, current))) {
      return prev;
    } else {
      return [
        ...prev,
        current
      ];
    }
  }, [] as T[]);
}
