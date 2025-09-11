/**
 * Componente Card reutilizable
 */

import React from "react";
import { cn } from "../../utils";

interface CardProps {
  children: React.ReactNode;
  className?: string;
  padding?: "none" | "sm" | "md" | "lg";
  shadow?: "none" | "sm" | "md" | "lg";
  hover?: boolean;
  clickable?: boolean;
  onClick?: () => void;
}

const Card: React.FC<CardProps> = ({
  children,
  className = "",
  padding = "md",
  shadow = "md",
  hover = false,
  clickable = false,
  onClick,
}) => {
  const baseClasses = "bg-white rounded-lg border border-gray-200";

  const paddingClasses = {
    none: "",
    sm: "p-3",
    md: "p-4",
    lg: "p-6",
  };

  const shadowClasses = {
    none: "",
    sm: "shadow-sm",
    md: "shadow-md",
    lg: "shadow-lg",
  };

  const interactionClasses = {
    hover: hover ? "hover:shadow-lg transition-shadow duration-200" : "",
    clickable: clickable ? "cursor-pointer hover:bg-gray-50" : "",
  };

  const cardClasses = cn(
    baseClasses,
    paddingClasses[padding],
    shadowClasses[shadow],
    interactionClasses.hover,
    interactionClasses.clickable,
    className
  );

  return (
    <div
      className={cardClasses}
      onClick={onClick}
      role={clickable ? "button" : undefined}
      tabIndex={clickable ? 0 : undefined}
    >
      {children}
    </div>
  );
};

interface CardHeaderProps {
  children: React.ReactNode;
  className?: string;
}

export const CardHeader: React.FC<CardHeaderProps> = ({
  children,
  className = "",
}) => {
  return (
    <div className={cn("border-b border-gray-200 pb-3 mb-4", className)}>
      {children}
    </div>
  );
};

interface CardTitleProps {
  children: React.ReactNode;
  className?: string;
  level?: 1 | 2 | 3 | 4 | 5 | 6;
}

export const CardTitle: React.FC<CardTitleProps> = ({
  children,
  className = "",
  level = 3,
}) => {
  const levelClasses = {
    1: "text-2xl",
    2: "text-xl",
    3: "text-lg",
    4: "text-base",
    5: "text-sm",
    6: "text-xs",
  };

  const HeadingComponent = ({
    children,
    className,
  }: {
    children: React.ReactNode;
    className: string;
  }) => {
    switch (level) {
      case 1:
        return <h1 className={className}>{children}</h1>;
      case 2:
        return <h2 className={className}>{children}</h2>;
      case 3:
        return <h3 className={className}>{children}</h3>;
      case 4:
        return <h4 className={className}>{children}</h4>;
      case 5:
        return <h5 className={className}>{children}</h5>;
      case 6:
        return <h6 className={className}>{children}</h6>;
      default:
        return <h3 className={className}>{children}</h3>;
    }
  };

  return (
    <HeadingComponent
      className={cn(
        "font-semibold text-gray-900",
        levelClasses[level],
        className
      )}
    >
      {children}
    </HeadingComponent>
  );
};

interface CardContentProps {
  children: React.ReactNode;
  className?: string;
}

export const CardContent: React.FC<CardContentProps> = ({
  children,
  className = "",
}) => {
  return <div className={cn("text-gray-600", className)}>{children}</div>;
};

interface CardFooterProps {
  children: React.ReactNode;
  className?: string;
}

export const CardFooter: React.FC<CardFooterProps> = ({
  children,
  className = "",
}) => {
  return (
    <div className={cn("border-t border-gray-200 pt-3 mt-4", className)}>
      {children}
    </div>
  );
};

export default Card;
