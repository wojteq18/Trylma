use super::message::message;

pub struct move_message {
    pub from: (u8, u8),
    pub to: (u8, u8),
}

impl move_message {
    // Konstruktor `new` dla `move_message`
    pub fn new(from: (u8, u8), to: (u8, u8)) -> Self {
        Self { from, to }
    }
}

impl message for move_message {
    fn get_content(&self) -> String {
        format!("move {} {} {} {}", self.from.0, self.from.1, self.to.0, self.to.1)
    }
}
