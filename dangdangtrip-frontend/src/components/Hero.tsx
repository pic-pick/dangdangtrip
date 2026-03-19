import heroBackground from "../assets/hero_background.jpg";
import styles from "./Hero.module.css";
import { useNavigate } from "react-router-dom";

interface HeroProps {
  keyword: string;
  onKeywordChange: (value: string) => void;
}

export default function Hero({ keyword, onKeywordChange }: HeroProps) {
  const navigate = useNavigate();

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (!keyword.trim()) return;
    navigate(`/search?keyword=${encodeURIComponent(keyword.trim())}`);
  };

  return (
    <section
      className={styles.hero}
      style={{ backgroundImage: `url(${heroBackground})` }}
    >
      <div className={styles.overlay} />

      <div className={styles.content}>
        <h1 className={styles.title}>반려동물과 함께하는 여행지 탐색</h1>
        <p className={styles.description}>
          지역이나 장소 이름을 검색해보세요.
        </p>

        <form className={styles.searchBox} onSubmit={handleSubmit}>
          <input
            type="text"
            value={keyword}
            onChange={(event) => onKeywordChange(event.target.value)}
            placeholder="서울, 제주, 한옥마을, 공원..."
            className={styles.input}
          />
          <button type="submit" className={styles.button}>
            검색
          </button>
        </form>
      </div>
    </section>
  );
}
