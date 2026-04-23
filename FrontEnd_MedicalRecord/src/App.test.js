import { render, screen } from '@testing-library/react';
import App from './App';

test('renders medical record heading', () => {
  render(<App />);
  const heading = screen.getByRole('heading', { name: /medical record/i });
  expect(heading).toBeInTheDocument();
});
