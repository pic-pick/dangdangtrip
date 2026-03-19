import styles from "./PlaceCard.module.css";

interface Place {
    contentId: string;
    title: string;
    addr1: string;
    image: string;
    overview: string;
}

const PLACEHOLDER = "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='400' height='288' viewBox='0 0 400 288'%3E%3Crect width='400' height='288' fill='%23f3f4f6'/%3E%3Ctext x='50%25' y='50%25' dominant-baseline='middle' text-anchor='middle' font-size='64'%3E🐾%3C/text%3E%3C/svg%3E";

export default function PlaceCard({ place }: { place: Place }) {
    return (
        <article className={styles.card}>
            <div className={styles.imageWrapper}>
                <img
                    src={place.image || PLACEHOLDER}
                    alt={place.title}
                    onError={(e) => { (e.currentTarget as HTMLImageElement).src = PLACEHOLDER; }}
                />
            </div>

            <div className={styles.content}>
                <h3>{place.title}</h3>
                <p className={styles.address}>{place.addr1}</p>
                <p className={styles.desc}>{place.overview}</p>
            </div>
        </article>
    );
}