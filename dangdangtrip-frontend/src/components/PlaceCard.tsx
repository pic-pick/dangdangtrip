import styles from "./PlaceCard.module.css";

interface Place {
    contentId: string;
    title: string;
    addr1: string;
    image: string;
    overview: string;
}

export default function PlaceCard({ place }: { place: Place }) {
    return (
        <article className={styles.card}>
            <div className={styles.imageWrapper}>
                <img src={place.image} alt={place.title} />
            </div>

            <div className={styles.content}>
                <h3>{place.title}</h3>
                <p className={styles.address}>{place.addr1}</p>
                <p className={styles.desc}>{place.overview}</p>
            </div>
        </article>
    );
}