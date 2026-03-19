import { useRef, useState, useEffect } from "react";
import styles from "./RegionList.module.css";

const REGION_IMAGES: Record<string, string> = {
    "서울":           "http://tong.visitkorea.or.kr/cms/resource/94/2932494_image2_1.bmp",
    "인천":           "http://tong.visitkorea.or.kr/cms/resource/13/3537413_image2_1.jpg",
    "대전":           "http://tong.visitkorea.or.kr/cms/resource/06/3565206_image2_1.jpg",
    "대구":           "http://tong.visitkorea.or.kr/cms/resource/44/3310544_image2_1.jpg",
    "광주":           "http://tong.visitkorea.or.kr/cms/resource/79/3380379_image2_1.png",
    "부산":           "http://tong.visitkorea.or.kr/cms/resource/42/3071042_image2_1.JPG",
    "울산":           "http://tong.visitkorea.or.kr/cms/resource/02/3078002_image2_1.bmp",
    "세종특별자치시":  "http://tong.visitkorea.or.kr/cms/resource/76/3353976_image2_1.jpg",
    "경기도":         "http://tong.visitkorea.or.kr/cms/resource/38/3547138_image2_1.jpg",
    "강원특별자치도":  "http://tong.visitkorea.or.kr/cms/resource/72/3388672_image2_1.JPG",
    "충청북도":       "http://tong.visitkorea.or.kr/cms/resource/57/3581957_image2_1.jpg",
    "충청남도":       "http://tong.visitkorea.or.kr/cms/resource/10/3340710_image2_1.jpg",
    "경상북도":       "http://tong.visitkorea.or.kr/cms/resource/95/3407895_image2_1.png",
    "경상남도":       "http://tong.visitkorea.or.kr/cms/resource/38/3573038_image2_1.jpg",
    "전북특별자치도":  "http://tong.visitkorea.or.kr/cms/resource/82/3364482_image2_1.JPG",
    "전라남도":       "http://tong.visitkorea.or.kr/cms/resource/19/3018819_image2_1.jpg",
    "제주특별자치도":  "http://tong.visitkorea.or.kr/cms/resource/45/3523545_image2_1.jpg",
};

interface RegionListProps {
    regions: string[];
    onSelect?: (name: string) => void;
    selectedRegion?: string;
}

const DRAG_THRESHOLD = 5;

export default function RegionList({ regions, onSelect, selectedRegion }: RegionListProps) {
    const wrapperRef = useRef<HTMLDivElement | null>(null);
    const isDraggingRef = useRef(false);
    const hasDraggedRef = useRef(false);
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
        hasDraggedRef.current = false;
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
        if (Math.abs(walk) > DRAG_THRESHOLD) {
            hasDraggedRef.current = true;
        }
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
                        <button
                            key={region}
                            className={`${styles.button} ${selectedRegion === region ? styles.selected : ""}`}
                            onClick={() => { if (!hasDraggedRef.current) onSelect?.(region); }}
                        >
                            <span className={styles.circle}>
                                {REGION_IMAGES[region] && (
                                    <img
                                        src={REGION_IMAGES[region]}
                                        alt={region}
                                        draggable={false}
                                        className={styles.circleImg}
                                        onError={(e) => { (e.currentTarget as HTMLImageElement).style.display = "none"; }}
                                    />
                                )}
                            </span>
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