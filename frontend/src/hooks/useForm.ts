/**
 * Hook personalizado para manejo de formularios
 */

import { useState, useCallback } from "react";

interface UseFormProps<T> {
  initialValues: T;
  validate?: (values: T) => Record<string, string>;
  onSubmit: (values: T) => void | Promise<void>;
}

interface UseFormReturn<T> {
  values: T;
  errors: Record<string, string>;
  touched: Record<string, boolean>;
  isSubmitting: boolean;
  setValue: (name: keyof T, value: T[keyof T]) => void;
  setValues: (values: Partial<T>) => void;
  setError: (name: keyof T, error: string) => void;
  clearErrors: () => void;
  handleChange: (name: keyof T) => (value: T[keyof T]) => void;
  handleBlur: (name: keyof T) => () => void;
  handleSubmit: (e?: React.FormEvent) => Promise<void>;
  reset: () => void;
  isValid: boolean;
  isDirty: boolean;
}

export const useForm = <T extends Record<string, unknown>>({
  initialValues,
  validate,
  onSubmit,
}: UseFormProps<T>): UseFormReturn<T> => {
  const [values, setValuesState] = useState<T>(initialValues);
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [touched, setTouched] = useState<Record<string, boolean>>({});
  const [isSubmitting, setIsSubmitting] = useState(false);

  const setValue = useCallback((name: keyof T, value: T[keyof T]) => {
    setValuesState((prev) => ({ ...prev, [name]: value }));
  }, []);

  const setValues = useCallback((newValues: Partial<T>) => {
    setValuesState((prev) => ({ ...prev, ...newValues }));
  }, []);

  const setError = useCallback((name: keyof T, error: string) => {
    setErrors((prev) => ({ ...prev, [name as string]: error }));
  }, []);

  const clearErrors = useCallback(() => {
    setErrors({});
  }, []);

  const handleChange = useCallback(
    (name: keyof T) => (value: T[keyof T]) => {
      setValue(name, value);

      // Limpiar error del campo cuando cambia
      if (errors[name as string]) {
        setErrors((prev) => {
          const newErrors = { ...prev };
          delete newErrors[name as string];
          return newErrors;
        });
      }
    },
    [setValue, errors]
  );

  const handleBlur = useCallback(
    (name: keyof T) => () => {
      setTouched((prev) => ({ ...prev, [name as string]: true }));

      // Validar campo individual al perder foco
      if (validate) {
        const fieldErrors = validate(values);
        if (fieldErrors[name as string]) {
          setError(name, fieldErrors[name as string]);
        }
      }
    },
    [values, validate, setError]
  );

  const handleSubmit = useCallback(
    async (e?: React.FormEvent) => {
      if (e) {
        e.preventDefault();
      }

      setIsSubmitting(true);
      clearErrors();

      // Marcar todos los campos como touched
      const allTouched: Record<string, boolean> = {};
      Object.keys(values).forEach((key) => {
        allTouched[key] = true;
      });
      setTouched(allTouched);

      // Validar formulario
      if (validate) {
        const validationErrors = validate(values);
        if (Object.keys(validationErrors).length > 0) {
          setErrors(validationErrors);
          setIsSubmitting(false);
          return;
        }
      }

      try {
        await onSubmit(values);
      } catch (error) {
        console.error("Error submitting form:", error);
      } finally {
        setIsSubmitting(false);
      }
    },
    [values, validate, onSubmit, clearErrors]
  );

  const reset = useCallback(() => {
    setValuesState(initialValues);
    setErrors({});
    setTouched({});
    setIsSubmitting(false);
  }, [initialValues]);

  const isValid = Object.keys(errors).length === 0;

  const isDirty = JSON.stringify(values) !== JSON.stringify(initialValues);

  return {
    values,
    errors,
    touched,
    isSubmitting,
    setValue,
    setValues,
    setError,
    clearErrors,
    handleChange,
    handleBlur,
    handleSubmit,
    reset,
    isValid,
    isDirty,
  };
};
