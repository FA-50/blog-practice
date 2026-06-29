document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("signupForm");
    const button = document.getElementById("signupButton");
    const error = document.getElementById("signupError");

    form.addEventListener("submit", async event => {
        event.preventDefault();
        error.textContent = "";

        const password = document.getElementById("password").value;
        const passwordConfirm = document.getElementById("passwordConfirm").value;
        if (password !== passwordConfirm) {
            error.textContent = "비밀번호가 일치하지 않습니다.";
            return;
        }

        button.disabled = true;
        try {
            const response = await fetch("/api/members", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({
                    id: document.getElementById("userId").value.trim(),
                    password,
                    name: document.getElementById("name").value.trim()
                })
            });

            if (!response.ok) {
                const body = await response.json().catch(() => ({}));
                throw new Error(body.message || "회원가입을 처리할 수 없습니다.");
            }

            window.location.replace("/login?registered=true");
        } catch (exception) {
            error.textContent = exception.message;
        } finally {
            button.disabled = false;
        }
    });
});
