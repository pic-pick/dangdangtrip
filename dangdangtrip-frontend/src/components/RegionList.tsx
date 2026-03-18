import { useRef, useState, useEffect } from "react";
import styles from "./RegionList.module.css";

export default function RegionList({ regions }: { regions: string[] }) {
    const wrapperRef = useRef<HTMLDivElement | null>(null);
    const isDraggingRef = useRef(false);
    const startXRef = useRef(0);
    const scrollLeftRef = useRef(0);

    const [isDragging, setIsDragging] = useState(false);
    const [canScrollLeft, setCanScrollLeft] = useState(false);
    const [canScrollRight, setCanScrollRight] = useState(false);

    const updateScrollButtons = () => {
        if (!wrapperRef.current) return;

        const { scrollLeft, scrollWidth, clientWidth } = wrapperRef.current;

        setCanScrollLeft(scrollLeft > 0);
        setCanScrollRight(scrollLeft < scrollWidth - clientWidth - 1);
    };

    useEffect(() => {
        updateScrollButtons();

        const handleResize = () => updateScrollButtons();
        window.addEventListener("resize", handleResize);

        return () => window.removeEventListener("resize", handleResize);
    }, []);

    const scrollByAmount = (direction: "left" | "right") => {
        if (!wrapperRef.current) return;

        const amount = wrapperRef.current.clientWidth * 0.8;
        wrapperRef.current.scrollBy({
            left: direction === "left" ? -amount : amount,
            behavior: "smooth",
        });
    };

    const handleMouseDown = (event: React.MouseEvent<HTMLDivElement>) => {
        if (!wrapperRef.current) return;

        isDraggingRef.current = true;
        setIsDragging(true);
        startXRef.current = event.pageX - wrapperRef.current.offsetLeft;
        scrollLeftRef.current = wrapperRef.current.scrollLeft;
        wrapperRef.current.classList.add(styles.dragging);
    };

    const handleMouseLeave = () => {
        if (!wrapperRef.current) return;

        isDraggingRef.current = false;
        setIsDragging(false);
        wrapperRef.current.classList.remove(styles.dragging);
    };

    const handleMouseUp = () => {
        if (!wrapperRef.current) return;

        isDraggingRef.current = false;
        setIsDragging(false);
        wrapperRef.current.classList.remove(styles.dragging);
    };

    const handleMouseMove = (event: React.MouseEvent<HTMLDivElement>) => {
        if (!isDraggingRef.current || !wrapperRef.current) return;

        event.preventDefault();
        const x = event.pageX - wrapperRef.current.offsetLeft;
        const walk = (x - startXRef.current) * 1.2;
        wrapperRef.current.scrollLeft = scrollLeftRef.current - walk;
    };

    return (
        <div className={styles.outer}>
            {canScrollLeft && (
                <button
                    type="button"
                    className={`${styles.arrowButton} ${styles.leftButton}`}
                    onClick={() => scrollByAmount("left")}
                    aria-label="왼쪽으로 이동"
                >
                    ‹
                </button>
            )}

            <div
                ref={wrapperRef}
                className={`${styles.wrapper} ${isDragging ? styles.dragging : ""}`}
                onMouseDown={handleMouseDown}
                onMouseLeave={handleMouseLeave}
                onMouseUp={handleMouseUp}
                onMouseMove={handleMouseMove}
                onScroll={updateScrollButtons}
            >
                <div className={styles.container}>
                    {regions.map((region) => (
                        <button key={region} className={styles.button}>
                            <span className={styles.circle} />
                            <span className={styles.label}>{region}</span>
                        </button>
                    ))}
                </div>
            </div>

            {canScrollRight && (
                <button
                    type="button"
                    className={`${styles.arrowButton} ${styles.rightButton}`}
                    onClick={() => scrollByAmount("right")}
                    aria-label="오른쪽으로 이동"
                >
                    ›
                </button>
            )}
        </div>
    );
}