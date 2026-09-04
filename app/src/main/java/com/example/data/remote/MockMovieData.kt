package com.example.data.remote

import com.example.data.models.Movie
import com.example.data.models.MovieServer
import com.example.data.models.Episode

object MockMovieData {
    val movies = listOf(
        Movie(
            id = "1",
            name = "Mai",
            originName = "Mai",
            slug = "mai",
            content = "Mai là câu chuyện về cuộc đời đầy thăng trầm của một người phụ nữ xinh đẹp nhưng mang nhiều vết thương lòng. Khi cô gặp Dương, một chàng trai trẻ giàu sức sống và lãng tử, cuộc đời cô dường như bước sang trang mới. Nhưng những định kiến của xã hội, gánh nặng gia đình và bí mật trong quá khứ một lần nữa đẩy Mai vào những thử thách nghiệt ngã. Một tác phẩm điện ảnh sâu sắc, đầy xúc động của đạo diễn Trấn Thành.",
            type = "single",
            status = "completed",
            thumbUrl = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=800&auto=format&fit=crop&q=60",
            posterUrl = "https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?w=800&auto=format&fit=crop&q=60",
            year = 2024,
            quality = "FHD",
            lang = "Vietsub",
            duration = "131 phút",
            episodeCurrent = "Full",
            ratingImdb = 7.5,
            ratingTmdb = 7.8,
            categories = listOf("Tình Cảm", "Tâm Lý", "Chiếu Rạp"),
            countries = listOf("Việt Nam"),
            actors = listOf("Phương Anh Đào", "Tuấn Trần", "Hồng Đào", "Trấn Thành"),
            directors = listOf("Trấn Thành"),
            servers = listOf(
                MovieServer(
                    serverName = "VIP Stream",
                    episodes = listOf(
                        Episode(name = "Bản Đẹp FHD", slug = "full", m3u8Url = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8")
                    )
                )
            )
        ),
        Movie(
            id = "2",
            name = "Lật Mặt 7: Một Điều Ước",
            originName = "Face Off 7: One Wish",
            slug = "lat-mat-7-mot-dieu-uoc",
            content = "Lật Mặt 7 xoay quanh cuộc đời của bà Hai, một người mẹ già tần tảo một mình nuôi lớn 5 người con. Đến khi các con khôn lớn, dựng vợ gả chồng và lập nghiệp ở khắp mọi miền tổ quốc, mỗi người một hoàn cảnh riêng. Khi bà Hai gặp tai nạn, một câu hỏi lớn xuất hiện: ai sẽ là người chăm sóc mẹ già? Tác phẩm chạm đến trái tim khán giả về tình mẫu tử thiêng liêng và thực tế cuộc sống gia đình hiện đại.",
            type = "single",
            status = "completed",
            thumbUrl = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=800&auto=format&fit=crop&q=60",
            posterUrl = "https://images.unsplash.com/photo-1598899134739-24c46f58b8c0?w=800&auto=format&fit=crop&q=60",
            year = 2024,
            quality = "FHD",
            lang = "Vietsub",
            duration = "138 phút",
            episodeCurrent = "Full",
            ratingImdb = 8.1,
            ratingTmdb = 8.3,
            categories = listOf("Gia Đình", "Tâm Lý", "Kịch Tính"),
            countries = listOf("Việt Nam"),
            actors = listOf("Thanh Hiền", "Trương Minh Cường", "Đinh Y Nhung", "Quách Ngọc Tuyên"),
            directors = listOf("Lý Hải"),
            servers = listOf(
                MovieServer(
                    serverName = "HLS Server",
                    episodes = listOf(
                        Episode(name = "Bản Đẹp", slug = "full", m3u8Url = "https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.ism/.m3u8")
                    )
                )
            )
        ),
        Movie(
            id = "3",
            name = "Bố Già",
            originName = "Dad, I'm Sorry",
            slug = "bo-gia",
            content = "Câu chuyện xoay quanh Ba Sang - một người cha già đơn thân làm nghề chở hàng thuê tại một xóm lao động nghèo ở Sài Gòn. Ba Sang tuy nghèo nhưng giàu lòng nhân ái, luôn bao dung cho người khác kể cả khi bản thân chịu thiệt thòi. Tuy nhiên, sự khác biệt về khoảng cách thế hệ và tư duy sống khiến ông thường xuyên xung đột gay gắt với đứa con trai duy nhất tên Quắn - một vlogger kiếm tiền trực tuyến tự do. Bộ phim lấy nước mắt hàng triệu người xem về tình cha con.",
            type = "single",
            status = "completed",
            thumbUrl = "https://images.unsplash.com/photo-1542204172-e7052809a937?w=800&auto=format&fit=crop&q=60",
            posterUrl = "https://images.unsplash.com/photo-1533928298208-27ff66555d8d?w=800&auto=format&fit=crop&q=60",
            year = 2021,
            quality = "FHD",
            lang = "Vietsub",
            duration = "128 phút",
            episodeCurrent = "Full",
            ratingImdb = 7.2,
            ratingTmdb = 7.5,
            categories = listOf("Gia Đình", "Tình Cảm", "Hài Hước"),
            countries = listOf("Việt Nam"),
            actors = listOf("Trấn Thành", "Tuấn Trần", "Ngân Chi", "NSND Ngọc Giàu"),
            directors = listOf("Trấn Thành", "Vũ Ngọc Đãng"),
            servers = listOf(
                MovieServer(
                    serverName = "CDN Stream",
                    episodes = listOf(
                        Episode(name = "Full HD", slug = "full", m3u8Url = "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8")
                    )
                )
            )
        ),
        Movie(
            id = "4",
            name = "Băng Đảng Sài Gòn",
            originName = "Saigon Gangs",
            slug = "bang-dang-sai-gon",
            content = "Một câu chuyện giả tưởng kịch tính về cuộc chiến quyền lực trong thế giới ngầm tại Sài Gòn. Những băng nhóm trẻ tuổi nổi loạn đối đầu với các thế lực kỳ cựu để giành quyền kiểm soát địa bàn. Những pha hành động mãn nhãn kết hợp với những bài học sâu sắc về tình anh em, sự phản bội và cái giá phải trả của lòng tham.",
            type = "series",
            status = "ongoing",
            thumbUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=800&auto=format&fit=crop&q=60",
            posterUrl = "https://images.unsplash.com/photo-1508700115892-45ecd05ae2ad?w=800&auto=format&fit=crop&q=60",
            year = 2023,
            quality = "HD",
            lang = "Vietsub",
            duration = "45 phút / tập",
            episodeCurrent = "3 / 10 Tập",
            ratingImdb = 6.8,
            ratingTmdb = 7.0,
            categories = listOf("Hành Động", "Kịch Tính", "Hình Sự"),
            countries = listOf("Việt Nam"),
            actors = listOf("Lãnh Thanh", "Khương Ngọc", "Thạch Kim Long"),
            directors = listOf("Dustin Nguyễn"),
            servers = listOf(
                MovieServer(
                    serverName = "Tập phim",
                    episodes = listOf(
                        Episode(name = "Tập 1", slug = "tap-1", m3u8Url = "https://devstreaming-cdn.apple.com/videos/streaming/examples/img_bipbop_adv_example_fmp4/master.m3u8"),
                        Episode(name = "Tập 2", slug = "tap-2", m3u8Url = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"),
                        Episode(name = "Tập 3", slug = "tap-3", m3u8Url = "https://d2zihajmogu5jn.cloudfront.net/bipbop/bipbopall.m3u8")
                    )
                )
            )
        ),
        Movie(
            id = "5",
            name = "Doraemon: Bản Tình Ca Đất Nước",
            originName = "Doraemon: Symphony of the Earth",
            slug = "doraemon-ban-tinh-ca-dat-nuoc",
            content = "Doraemon và những người bạn quen thuộc: Nobita, Shizuka, Gian và Suneo bắt đầu một cuộc phiêu lưu âm nhạc tuyệt vời để cứu thế giới khỏi mối đe dọa vũ trụ nguy hiểm. Bằng cách sử dụng các bảo bối thần kỳ kỳ diệu của Doraemon kết hợp với sức mạnh của âm nhạc chân thành, họ khám phá ra sức mạnh của sự kết nối giữa con người và tự nhiên.",
            type = "single",
            status = "completed",
            thumbUrl = "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=800&auto=format&fit=crop&q=60",
            posterUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=800&auto=format&fit=crop&q=60",
            year = 2024,
            quality = "FHD",
            lang = "Thuyết Minh",
            duration = "115 phút",
            episodeCurrent = "Full Thuyết Minh",
            ratingImdb = 7.9,
            ratingTmdb = 8.1,
            categories = listOf("Hoạt Hình", "Phiêu Lưu", "Gia Đình"),
            countries = listOf("Nhật Bản"),
            actors = listOf("Doraemon", "Nobita", "Shizuka", "Gian", "Suneo"),
            directors = listOf("Kazuaki Imai"),
            servers = listOf(
                MovieServer(
                    serverName = "Anilife",
                    episodes = listOf(
                        Episode(name = "Thuyết Minh HD", slug = "full", m3u8Url = "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8")
                    )
                )
            )
        ),
        Movie(
            id = "6",
            name = "Kẻ Hủy Diệt Thời Gian",
            originName = "Tears of Steel",
            slug = "tears-of-steel",
            content = "Trong một bối cảnh tương lai đầy biến động của thành phố Amsterdam cổ kính, một nhóm các nhà khoa học và chiến binh trẻ tìm cách ngăn chặn thảm họa robot hủy diệt bằng một phát minh du hành thời gian tối mật. Tác phẩm khoa học viễn xuôi mãn nhãn với kỹ xảo ấn tượng và cốt truyện căng thẳng đến từng giây.",
            type = "single",
            status = "completed",
            thumbUrl = "https://images.unsplash.com/photo-1478760329108-5c3ed9d495a0?w=800&auto=format&fit=crop&q=60",
            posterUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800&auto=format&fit=crop&q=60",
            year = 2012,
            quality = "UHD",
            lang = "Vietsub",
            duration = "12 phút",
            episodeCurrent = "Full",
            ratingImdb = 8.5,
            ratingTmdb = 8.6,
            categories = listOf("Khoa Học Viễn Tưởng", "Hành Động"),
            countries = listOf("Mỹ", "Hà Lan"),
            actors = listOf("Derek de Lint", "Rogier Schippers"),
            directors = listOf("Ian Hubert"),
            servers = listOf(
                MovieServer(
                    serverName = "Default Server",
                    episodes = listOf(
                        Episode(name = "Full Bản Gốc", slug = "full", m3u8Url = "https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.ism/.m3u8")
                    )
                )
            )
        )
    )
    
    val allCategories = listOf("Hành Động", "Tình Cảm", "Tâm Lý", "Chiếu Rạp", "Gia Đình", "Kịch Tính", "Hoạt Hình", "Phiêu Lưu", "Khoa Học Viễn Tưởng", "Hình Sự")
    val allCountries = listOf("Việt Nam", "Nhật Bản", "Mỹ", "Hàn Quốc", "Trung Quốc")
}
